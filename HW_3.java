import java.util.*;

// Перечисления
enum FuelType {
    PETROL, DIESEL, ELECTRICITY, KEROSENE, WIND
}

enum TransportType {
    GROUND, AIR, WATER
}

// Запечатанные классы
sealed abstract class Engine permits CombustionEngine, ElectricEngine, JetEngine {
    protected final String model;
    protected final double power;
    
    public Engine(String model, double power) {
        this.model = model;
        this.power = power;
    }
    
    public abstract void start();
    public abstract void stop();
    
    public String getModel() { return model; }
    public double getPower() { return power; }
}

final class CombustionEngine extends Engine {
    private final FuelType fuelType;
    
    public CombustionEngine(String model, double power, FuelType fuelType) {
        super(model, power);
        this.fuelType = fuelType;
    }
    
    @Override
    public void start() {
        System.out.println("Запуск двигателя внутреннего сгорания " + model);
    }
    
    @Override
    public void stop() {
        System.out.println("Остановка двигателя внутреннего сгорания " + model);
    }
    
    public FuelType getFuelType() { return fuelType; }
}

final class ElectricEngine extends Engine {
    private final int batteryCapacity;
    
    public ElectricEngine(String model, double power, int batteryCapacity) {
        super(model, power);
        this.batteryCapacity = batteryCapacity;
    }
    
    @Override
    public void start() {
        System.out.println("Запуск электродвигателя " + model);
    }
    
    @Override
    public void stop() {
        System.out.println("Остановка электродвигателя " + model);
    }
    
    public int getBatteryCapacity() { return batteryCapacity; }
}

final class JetEngine extends Engine {
    public JetEngine(String model, double power) {
        super(model, power);
    }
    
    @Override
    public void start() {
        System.out.println("Запуск реактивного двигателя " + model);
    }
    
    @Override
    public void stop() {
        System.out.println("Остановка реактивного двигателя " + model);
    }
}

// Абстрактный класс транспорта
sealed abstract class Transport permits Car, Bicycle, Airplane, Ship {
    protected final String name;
    protected final int maxSpeed;
    protected final TransportType type;
    protected Engine engine;
    
    public Transport(String name, int maxSpeed, TransportType type, Engine engine) {
        this.name = name;
        this.maxSpeed = maxSpeed;
        this.type = type;
        this.engine = engine;
    }
    
    public abstract void move();
    public abstract void stop();
    
    public void displayInfo() {
        System.out.println("Транспорт: " + name);
        System.out.println("Тип: " + type);
        System.out.println("Макс. скорость: " + maxSpeed + " км/ч");
        if (engine != null) {
            System.out.println("Двигатель: " + engine.getModel() + " (" + engine.getPower() + " л.с.)");
        }
    }
    
    public String getName() { return name; }
    public int getMaxSpeed() { return maxSpeed; }
    public TransportType getType() { return type; }
    public Engine getEngine() { return engine; }
}

// Конкретные классы транспорта
final class Car extends Transport {
    private final int doors;
    private final FuelType fuelType;
    
    public Car(String name, int maxSpeed, int doors, FuelType fuelType, Engine engine) {
        super(name, maxSpeed, TransportType.GROUND, engine);
        this.doors = doors;
        this.fuelType = fuelType;
    }
    
    @Override
    public void move() {
        System.out.println(name + " едет по дороге");
        if (engine != null) engine.start();
    }
    
    @Override
    public void stop() {
        System.out.println(name + " остановился");
        if (engine != null) engine.stop();
    }
    
    public void honk() {
        System.out.println(name + " сигналит: Би-бип!");
    }
    
    public int getDoors() { return doors; }
    public FuelType getFuelType() { return fuelType; }
}

final class Bicycle extends Transport {
    private final int gears;
    
    public Bicycle(String name, int maxSpeed, int gears) {
        super(name, maxSpeed, TransportType.GROUND, null);
        this.gears = gears;
    }
    
    @Override
    public void move() {
        System.out.println(name + " едет, педали крутятся");
    }
    
    @Override
    public void stop() {
        System.out.println(name + " остановился");
    }
    
    public void ringBell() {
        System.out.println(name + " звонит в звонок: Дзинь-дзинь!");
    }
    
    public int getGears() { return gears; }
}

final class Airplane extends Transport {
    private final int wingspan;
    private final int maxAltitude;
    
    public Airplane(String name, int maxSpeed, int wingspan, int maxAltitude, Engine engine) {
        super(name, maxSpeed, TransportType.AIR, engine);
        this.wingspan = wingspan;
        this.maxAltitude = maxAltitude;
    }
    
    @Override
    public void move() {
        System.out.println(name + " взлетает в небо");
        if (engine != null) engine.start();
    }
    
    @Override
    public void stop() {
        System.out.println(name + " приземлился");
        if (engine != null) engine.stop();
    }
    
    public void takeOff() {
        System.out.println(name + " выполняет взлет");
    }
    
    public void land() {
        System.out.println(name + " выполняет посадку");
    }
    
    public int getWingspan() { return wingspan; }
    public int getMaxAltitude() { return maxAltitude; }
}

final class Ship extends Transport {
    private final int displacement;
    private final FuelType fuelType;
    
    public Ship(String name, int maxSpeed, int displacement, FuelType fuelType, Engine engine) {
        super(name, maxSpeed, TransportType.WATER, engine);
        this.displacement = displacement;
        this.fuelType = fuelType;
    }
    
    @Override
    public void move() {
        System.out.println(name + " плывет по воде");
        if (engine != null) engine.start();
    }
    
    @Override
    public void stop() {
        System.out.println(name + " остановился в порту");
        if (engine != null) engine.stop();
    }
    
    public void soundHorn() {
        System.out.println(name + " гудит: У-у-у-у!");
    }
    
    public int getDisplacement() { return displacement; }
    public FuelType getFuelType() { return fuelType; }
}

// Пользовательский интерфейс
public class HW_3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Transport> transports = new ArrayList<>();
        
        // Создаем несколько транспортных средств
        transports.add(new Car("Toyota Camry", 220, 4, FuelType.PETROL, 
            new CombustionEngine("V6", 250, FuelType.PETROL)));
        transports.add(new Car("Tesla Model S", 250, 4, FuelType.ELECTRICITY,
            new ElectricEngine("Dual Motor", 500, 100)));
        transports.add(new Bicycle("Stels Navigator", 35, 21));
        transports.add(new Airplane("Boeing 737", 850, 35, 12500,
            new JetEngine("CFM56", 12000)));
        transports.add(new Ship("Titanic", 42, 52310, FuelType.DIESEL,
            new CombustionEngine("Triple Expansion", 46000, FuelType.DIESEL)));
        
        while (true) {
            System.out.println("\n=== Система управления транспортом ===");
            System.out.println("1. Показать весь транспорт");
            System.out.println("2. Управлять транспортом");
            System.out.println("3. Добавить новый транспорт");
            System.out.println("4. Выход");
            System.out.print("Выберите действие: ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    showAllTransports(transports);
                    break;
                case 2:
                    manageTransport(transports, scanner);
                    break;
                case 3:
                    addNewTransport(transports, scanner);
                    break;
                case 4:
                    System.out.println("Выход из системы...");
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
    
    private static void showAllTransports(List<Transport> transports) {
        System.out.println("\n=== Весь транспорт ===");
        for (int i = 0; i < transports.size(); i++) {
            System.out.println((i + 1) + ". " + transports.get(i).getName());
            transports.get(i).displayInfo();
            System.out.println("---");
        }
    }
    
    private static void manageTransport(List<Transport> transports, Scanner scanner) {
        if (transports.isEmpty()) {
            System.out.println("Нет доступного транспорта!");
            return;
        }
        
        System.out.println("\nВыберите транспорт:");
        for (int i = 0; i < transports.size(); i++) {
            System.out.println((i + 1) + ". " + transports.get(i).getName());
        }
        
        int index = scanner.nextInt() - 1;
        if (index < 0 || index >= transports.size()) {
            System.out.println("Неверный выбор!");
            return;
        }
        
        Transport transport = transports.get(index);
        
        System.out.println("\nУправление: " + transport.getName());
        System.out.println("1. Двигаться");
        System.out.println("2. Остановиться");
        System.out.println("3. Информация");
        
        if (transport instanceof Car) {
            System.out.println("4. Подать сигнал");
        } else if (transport instanceof Bicycle) {
            System.out.println("4. Позвонить в звонок");
        } else if (transport instanceof Airplane) {
            System.out.println("4. Взлететь");
            System.out.println("5. Приземлиться");
        } else if (transport instanceof Ship) {
            System.out.println("4. Подать гудок");
        }
        
        int action = scanner.nextInt();
        
        switch (action) {
            case 1:
                transport.move();
                break;
            case 2:
                transport.stop();
                break;
            case 3:
                transport.displayInfo();
                break;
            case 4:
                if (transport instanceof Car) {
                    ((Car) transport).honk();
                } else if (transport instanceof Bicycle) {
                    ((Bicycle) transport).ringBell();
                } else if (transport instanceof Airplane) {
                    ((Airplane) transport).takeOff();
                } else if (transport instanceof Ship) {
                    ((Ship) transport).soundHorn();
                }
                break;
            case 5:
                if (transport instanceof Airplane) {
                    ((Airplane) transport).land();
                }
                break;
            default:
                System.out.println("Неверное действие!");
        }
    }
    
    private static void addNewTransport(List<Transport> transports, Scanner scanner) {
        System.out.println("\n=== Добавление нового транспорта ===");
        System.out.println("1. Автомобиль");
        System.out.println("2. Велосипед");
        System.out.println("3. Самолет");
        System.out.println("4. Корабль");
        System.out.print("Выберите тип: ");
        
        int type = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        System.out.print("Введите название: ");
        String name = scanner.nextLine();
        System.out.print("Введите максимальную скорость: ");
        int maxSpeed = scanner.nextInt();
        
        switch (type) {
            case 1:
                System.out.print("Введите количество дверей: ");
                int doors = scanner.nextInt();
                System.out.print("Тип топлива (1-Бензин, 2-Дизель, 3-Электричество): ");
                FuelType fuelType = getFuelType(scanner.nextInt());
                Engine carEngine = createEngine(scanner, fuelType);
                transports.add(new Car(name, maxSpeed, doors, fuelType, carEngine));
                break;
            case 2:
                System.out.print("Введите количество передач: ");
                int gears = scanner.nextInt();
                transports.add(new Bicycle(name, maxSpeed, gears));
                break;
            case 3:
                System.out.print("Введите размах крыльев: ");
                int wingspan = scanner.nextInt();
                System.out.print("Введите максимальную высоту: ");
                int altitude = scanner.nextInt();
                Engine planeEngine = new JetEngine("JetEngine-" + name, 10000);
                transports.add(new Airplane(name, maxSpeed, wingspan, altitude, planeEngine));
                break;
            case 4:
                System.out.print("Введите водоизмещение: ");
                int displacement = scanner.nextInt();
                System.out.print("Тип топлива (1-Бензин, 2-Дизель): ");
                FuelType shipFuel = getFuelType(scanner.nextInt());
                Engine shipEngine = createEngine(scanner, shipFuel);
                transports.add(new Ship(name, maxSpeed, displacement, shipFuel, shipEngine));
                break;
            default:
                System.out.println("Неверный тип!");
        }
        
        System.out.println("Транспорт добавлен!");
    }
    
    private static FuelType getFuelType(int choice) {
        return switch (choice) {
            case 1 -> FuelType.PETROL;
            case 2 -> FuelType.DIESEL;
            case 3 -> FuelType.ELECTRICITY;
            default -> FuelType.PETROL;
        };
    }
    
    private static Engine createEngine(Scanner scanner, FuelType fuelType) {
        scanner.nextLine(); // consume newline
        System.out.print("Введите модель двигателя: ");
        String model = scanner.nextLine();
        System.out.print("Введите мощность: ");
        double power = scanner.nextDouble();
        
        return switch (fuelType) {
            case ELECTRICITY -> new ElectricEngine(model, power, 100);
            default -> new CombustionEngine(model, power, fuelType);
        };
    }
}
