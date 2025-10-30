import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Интерфейс Observer и Logger
interface Observer {
    void update(String message);
}

class Logger implements Observer {
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    @Override
    public void update(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.printf("[%s] %s%n", timestamp, message);
    }
}

// Класс Client
class Client {
    private final int id;
    private double balance;
    private final String currency;
    private final ReentrantLock lock;
    
    public Client(int id, double balance, String currency) {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
        this.lock = new ReentrantLock();
    }
    
    public void lock() {
        lock.lock();
    }
    
    public void unlock() {
        lock.unlock();
    }
    
    // Геттеры и сеттеры
    public int getId() { return id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    
    @Override
    public String toString() {
        return String.format("Client{id=%d, balance=%.2f %s}", id, balance, currency);
    }
}

// Абстрактный класс Transaction и его реализации
abstract class Transaction {
    protected final int id;
    protected final String type;
    protected static int nextId = 1;
    
    public Transaction(String type) {
        this.id = nextId++;
        this.type = type;
    }
    
    public abstract void process(Bank bank) throws Exception;
    
    public int getId() { return id; }
    public String getType() { return type; }
}

class CurrencyExchangeTransaction extends Transaction {
    private final int clientId;
    private final String fromCurrency;
    private final String toCurrency;
    private final double amount;
    
    public CurrencyExchangeTransaction(int clientId, String fromCurrency, 
                                     String toCurrency, double amount) {
        super("CURRENCY_EXCHANGE");
        this.clientId = clientId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }
    
    @Override
    public void process(Bank bank) throws Exception {
        bank.exchangeCurrency(clientId, fromCurrency, toCurrency, amount);
    }
    
    @Override
    public String toString() {
        return String.format("CurrencyExchange{id=%d, client=%d, %s->%s, amount=%.2f}", 
                           id, clientId, fromCurrency, toCurrency, amount);
    }
}

class TransferTransaction extends Transaction {
    private final int senderId;
    private final int receiverId;
    private final double amount;
    
    public TransferTransaction(int senderId, int receiverId, double amount) {
        super("TRANSFER");
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }
    
    @Override
    public void process(Bank bank) throws Exception {
        bank.transferFunds(senderId, receiverId, amount);
    }
    
    @Override
    public String toString() {
        return String.format("Transfer{id=%d, from=%d, to=%d, amount=%.2f}", 
                           id, senderId, receiverId, amount);
    }
}

class DepositTransaction extends Transaction {
    private final int clientId;
    private final double amount;
    
    public DepositTransaction(int clientId, double amount) {
        super("DEPOSIT");
        this.clientId = clientId;
        this.amount = amount;
    }
    
    @Override
    public void process(Bank bank) throws Exception {
        Client client = bank.getClient(clientId);
        if (client != null) {
            client.lock();
            try {
                client.setBalance(client.getBalance() + amount);
                bank.notifyObservers(String.format(
                    "Deposit successful: client %d +%.2f %s. New balance: %.2f",
                    clientId, amount, client.getCurrency(), client.getBalance()));
            } finally {
                client.unlock();
            }
        } else {
            throw new Exception("Client not found: " + clientId);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Deposit{id=%d, client=%d, amount=%.2f}", 
                           id, clientId, amount);
    }
}

class WithdrawalTransaction extends Transaction {
    private final int clientId;
    private final double amount;
    
    public WithdrawalTransaction(int clientId, double amount) {
        super("WITHDRAWAL");
        this.clientId = clientId;
        this.amount = amount;
    }
    
    @Override
    public void process(Bank bank) throws Exception {
        Client client = bank.getClient(clientId);
        if (client != null) {
            client.lock();
            try {
                if (client.getBalance() >= amount) {
                    client.setBalance(client.getBalance() - amount);
                    bank.notifyObservers(String.format(
                        "Withdrawal successful: client %d -%.2f %s. New balance: %.2f",
                        clientId, amount, client.getCurrency(), client.getBalance()));
                } else {
                    throw new Exception(String.format(
                        "Insufficient funds: client %d has %.2f %s, tried to withdraw %.2f",
                        clientId, client.getBalance(), client.getCurrency(), amount));
                }
            } finally {
                client.unlock();
            }
        } else {
            throw new Exception("Client not found: " + clientId);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Withdrawal{id=%d, client=%d, amount=%.2f}", 
                           id, clientId, amount);
    }
}

// Класс Cashier
class Cashier extends Thread {
    private final int id;
    private final Bank bank;
    private volatile boolean active;
    private int processedTransactions;
    
    public Cashier(int id, Bank bank) {
        this.id = id;
        this.bank = bank;
        this.active = true;
        this.processedTransactions = 0;
        this.setName("Cashier-" + id);
    }
    
    @Override
    public void run() {
        bank.notifyObservers("Cashier " + id + " started");
        
        while (active || !bank.getTransactionQueue().isEmpty()) {
            try {
                Transaction transaction = bank.getTransactionQueue().take();
                processTransaction(transaction);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        bank.notifyObservers("Cashier " + id + " stopped. Processed: " + processedTransactions);
    }
    
    private void processTransaction(Transaction transaction) {
        try {
            bank.notifyObservers("Cashier " + id + " processing: " + transaction);
            transaction.process(bank);
            processedTransactions++;
            bank.notifyObservers("Cashier " + id + " completed: " + transaction);
            
            // Имитация времени обработки
            Thread.sleep(100 + (long)(Math.random() * 200));
        } catch (Exception e) {
            bank.notifyObservers("Cashier " + id + " ERROR in " + transaction + ": " + e.getMessage());
        }
    }
    
    public void stopCashier() {
        this.active = false;
        this.interrupt();
    }
    
    public int getProcessedTransactions() {
        return processedTransactions;
    }
}

// Основной класс Bank
class Bank {
    private final ConcurrentHashMap<Integer, Client> clients;
    private final List<Cashier> cashiers;
    private final ConcurrentHashMap<String, Double> exchangeRates;
    private final BlockingQueue<Transaction> transactionQueue;
    private final List<Observer> observers;
    private final ReentrantLock observersLock;
    private final ScheduledExecutorService rateUpdateExecutor;
    private final Random random;
    
    public Bank(int numCashiers) {
        this.clients = new ConcurrentHashMap<>();
        this.cashiers = new CopyOnWriteArrayList<>();
        this.exchangeRates = new ConcurrentHashMap<>();
        this.transactionQueue = new LinkedBlockingQueue<>();
        this.observers = new ArrayList<>();
        this.observersLock = new ReentrantLock();
        this.rateUpdateExecutor = Executors.newScheduledThreadPool(1);
        this.random = new Random();
        
        initializeExchangeRates();
        startCashiers(numCashiers);
        startRateUpdates();
        addObserver(new Logger());
    }
    
    private void initializeExchangeRates() {
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        exchangeRates.put("GBP", 0.73);
        exchangeRates.put("JPY", 110.0);
        exchangeRates.put("RUB", 75.0);
    }
    
    private void startCashiers(int numCashiers) {
        for (int i = 1; i <= numCashiers; i++) {
            Cashier cashier = new Cashier(i, this);
            cashiers.add(cashier);
            cashier.start();
        }
    }
    
    private void startRateUpdates() {
        rateUpdateExecutor.scheduleAtFixedRate(() -> {
            updateExchangeRates();
        }, 0, 3, TimeUnit.SECONDS); // Обновляем каждые 3 секунды
    }
    
    private void updateExchangeRates() {
        for (String currency : exchangeRates.keySet()) {
            if (!currency.equals("USD")) {
                double currentRate = exchangeRates.get(currency);
                double change = (random.nextDouble() - 0.5) * 0.1; // ±5%
                double newRate = currentRate * (1 + change);
                exchangeRates.put(currency, newRate);
                
                notifyObservers(String.format(
                    "Exchange rate updated: %s/USD = %.4f (change: %.2f%%)",
                    currency, newRate, (change * 100)));
            }
        }
    }
    
    // Основные банковские операции
    public void exchangeCurrency(int clientId, String fromCurrency, 
                               String toCurrency, double amount) throws Exception {
        Client client = getClient(clientId);
        if (client == null) {
            throw new Exception("Client not found: " + clientId);
        }
        
        if (!client.getCurrency().equals(fromCurrency)) {
            throw new Exception(String.format(
                "Client %d currency mismatch: expected %s, actual %s",
                clientId, fromCurrency, client.getCurrency()));
        }
        
        client.lock();
        try {
            if (client.getBalance() < amount) {
                throw new Exception(String.format(
                    "Insufficient funds for exchange: client %d has %.2f %s, needs %.2f",
                    clientId, client.getBalance(), fromCurrency, amount));
            }
            
            double fromRate = exchangeRates.get(fromCurrency);
            double toRate = exchangeRates.get(toCurrency);
            double usdAmount = amount / fromRate;
            double convertedAmount = usdAmount * toRate;
            
            client.setBalance(client.getBalance() - amount);
            // В реальной системе здесь был бы новый клиент с другой валютой
            // Для упрощения просто изменяем баланс и валюту
            client.setBalance(convertedAmount);
            
            notifyObservers(String.format(
                "Currency exchange successful: client %d %.2f %s -> %.2f %s (rate: %.4f)",
                clientId, amount, fromCurrency, convertedAmount, toCurrency, toRate/fromRate));
                
        } finally {
            client.unlock();
        }
    }
    
    public void transferFunds(int senderId, int receiverId, double amount) throws Exception {
        if (senderId == receiverId) {
            throw new Exception("Cannot transfer to same account");
        }
        
        Client sender = getClient(senderId);
        Client receiver = getClient(receiverId);
        
        if (sender == null || receiver == null) {
            throw new Exception("Client not found: " + (sender == null ? senderId : receiverId));
        }
        
        // Чтобы избежать deadlock, блокируем счета в определенном порядке
        Client firstLock = sender.getId() < receiver.getId() ? sender : receiver;
        Client secondLock = sender.getId() < receiver.getId() ? receiver : sender;
        
        firstLock.lock();
        secondLock.lock();
        
        try {
            if (sender.getBalance() < amount) {
                throw new Exception(String.format(
                    "Insufficient funds for transfer: sender %d has %.2f %s, needs %.2f",
                    senderId, sender.getBalance(), sender.getCurrency(), amount));
            }
            
            // Конвертация валюты, если нужно
            double transferAmount = amount;
            if (!sender.getCurrency().equals(receiver.getCurrency())) {
                double senderRate = exchangeRates.get(sender.getCurrency());
                double receiverRate = exchangeRates.get(receiver.getCurrency());
                double usdAmount = amount / senderRate;
                transferAmount = usdAmount * receiverRate;
            }
            
            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + transferAmount);
            
            notifyObservers(String.format(
                "Transfer successful: %d -> %d %.2f %s (received: %.2f %s)",
                senderId, receiverId, amount, sender.getCurrency(), 
                transferAmount, receiver.getCurrency()));
                
        } finally {
            secondLock.unlock();
            firstLock.unlock();
        }
    }
    
    // Управление очередью транзакций
    public void addTransaction(Transaction transaction) {
        try {
            transactionQueue.put(transaction);
            notifyObservers("Transaction queued: " + transaction);
        } catch (Exception e) {
            notifyObservers("ERROR queueing transaction: " + e.getMessage());
        }
    }
    
    // Управление клиентами
    public void addClient(Client client) {
        clients.put(client.getId(), client);
        notifyObservers("Client added: " + client);
    }
    
    public Client getClient(int clientId) {
        return clients.get(clientId);
    }
    
    public Collection<Client> getAllClients() {
        return clients.values();
    }
    
    // Observer pattern implementation
    public void addObserver(Observer observer) {
        observersLock.lock();
        try {
            observers.add(observer);
        } finally {
            observersLock.unlock();
        }
    }
    
    public void notifyObservers(String message) {
        observersLock.lock();
        try {
            for (Observer observer : observers) {
                observer.update(message);
            }
        } finally {
            observersLock.unlock();
        }
    }
    
    // Геттеры
    public BlockingQueue<Transaction> getTransactionQueue() {
        return transactionQueue;
    }
    
    public Map<String, Double> getExchangeRates() {
        return new HashMap<>(exchangeRates);
    }
    
    // Завершение работы
    public void shutdown() {
        notifyObservers("Bank shutdown initiated");
        
        // Останавливаем кассиров
        for (Cashier cashier : cashiers) {
            cashier.stopCashier();
        }
        
        // Останавливаем обновление курсов
        rateUpdateExecutor.shutdown();
        try {
            if (!rateUpdateExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                rateUpdateExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            rateUpdateExecutor.shutdownNow();
        }
        
        notifyObservers("Bank shutdown completed");
    }
    
    public void waitUntilAllTransactionsProcessed() throws InterruptedException {
        while (!transactionQueue.isEmpty()) {
            Thread.sleep(100);
        }
        
        // Даем кассирам время завершить текущие операции
        Thread.sleep(1000);
    }
}

// Демонстрационный класс
public class HW_6 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Bank Simulation Started ===\n");
        
        // Создаем банк с 3 кассами
        Bank bank = new Bank(3);
        
        // Добавляем клиентов
        bank.addClient(new Client(1, 1000.0, "USD"));
        bank.addClient(new Client(2, 500.0, "EUR"));
        bank.addClient(new Client(3, 300.0, "GBP"));
        bank.addClient(new Client(4, 50000.0, "JPY"));
        bank.addClient(new Client(5, 25000.0, "RUB"));
        
        // Ждем немного для начала обновления курсов
        Thread.sleep(2000);
        
        // Добавляем различные транзакции
        System.out.println("\n=== Adding Transactions ===\n");
        
        // Депозиты
        bank.addTransaction(new DepositTransaction(1, 200.0));
        bank.addTransaction(new DepositTransaction(2, 150.0));
        
        // Снятия
        bank.addTransaction(new WithdrawalTransaction(1, 100.0));
        bank.addTransaction(new WithdrawalTransaction(3, 50.0));
        
        // Переводы
        bank.addTransaction(new TransferTransaction(1, 2, 150.0));
        bank.addTransaction(new TransferTransaction(2, 3, 75.0));
        
        // Обмен валют
        bank.addTransaction(new CurrencyExchangeTransaction(1, "USD", "EUR", 100.0));
        bank.addTransaction(new CurrencyExchangeTransaction(3, "GBP", "USD", 50.0));
        
        // Еще несколько операций
        bank.addTransaction(new DepositTransaction(4, 10000.0));
        bank.addTransaction(new TransferTransaction(4, 5, 5000.0));
        bank.addTransaction(new CurrencyExchangeTransaction(5, "RUB", "USD", 1000.0));
        
        // Ждем обработки всех транзакций
        bank.waitUntilAllTransactionsProcessed();
        
        // Показываем финальное состояние
        System.out.println("\n=== Final State ===\n");
        System.out.println("Exchange Rates:");
        bank.getExchangeRates().forEach((currency, rate) -> 
            System.out.printf("  %s/USD: %.4f%n", currency, rate));
        
        System.out.println("\nClient Balances:");
        bank.getAllClients().forEach(client -> 
            System.out.println("  " + client));
        
        // Завершаем работу
        Thread.sleep(1000);
        bank.shutdown();
        
        System.out.println("\n=== Bank Simulation Completed ===");
    }
}