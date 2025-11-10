import java.util.*;
import java.util.InputMismatchException;

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
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Модель двигателя не может быть пустой");
        }
        if (power <= 0) {
            throw new IllegalArgumentException("Мощность двигателя должна быть положительной");
        }
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
        if (fuelType == null) {
            throw new IllegalArgumentException("Тип топлива не может быть null");
        }
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
        if (batteryCapacity <= 0) {
            throw new IllegalArgumentException("Емкость батареи должна быть положительной");
        }
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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название транспорта не может быть пустым");
        }
        if (maxSpeed <= 0) {
            throw new IllegalArgumentException("Максимальная скорость должна быть положительной");
        }
        if (type == null) {
            throw new IllegalArgumentException("Тип транспорта не может быть null");
        }
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
        if (doors <= 0 || doors > 10) {
            throw new IllegalArgumentException("Количество дверей должно быть от 1 до 10");
        }
        if (fuelType == null) {
            throw new IllegalArgumentException("Тип топлива не может быть null");
        }
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
        if (gears <= 0 || gears > 30) {
            throw new IllegalArgumentException("Количество передач должно быть от 1 до 30");
        }
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
        if (wingspan <= 0) {
            throw new IllegalArgumentException("Размах крыльев должен быть положительным");
        }
        if (maxAltitude <= 0) {
            throw new IllegalArgumentException("Максимальная высота должна быть положительной");
        }
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
        if (displacement <= 0) {
            throw new IllegalArgumentException("Водоизмещение должно быть положительным");
        }
        if (fuelType == null) {
            throw new IllegalArgumentException("Тип топлива не может быть null");
        }
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
        try {
            transports.add(new Car("Toyota Camry", 220, 4, FuelType.PETROL, 
                new CombustionEngine("V6", 250, FuelType.PETROL)));
            transports.add(new Car("Tesla Model S", 250, 4, FuelType.ELECTRICITY,
                new ElectricEngine("Dual Motor", 500, 100)));
            transports.add(new Bicycle("Stels Navigator", 35, 21));
            transports.add(new Airplane("Boeing 737", 850, 35, 12500,
                new JetEngine("CFM56", 12000)));
            transports.add(new Ship("Titanic", 42, 52310, FuelType.DIESEL,
                new CombustionEngine("Triple Expansion", 46000, FuelType.DIESEL)));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании тестовых данных: " + e.getMessage());
        }
        
        while (true) {
            try {
                System.out.println("\n=== Система управления транспортом ===");
                System.out.println("1. Показать весь транспорт");
                System.out.println("2. Управлять транспортом");
                System.out.println("3. Добавить новый транспорт");
                System.out.println("4. Выход");
                System.out.print("Выберите действие: ");
                
                int choice = readIntInput(scanner, 1, 4);
                
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
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
                scanner.nextLine(); // Очистка буфера
            }
        }
    }
    
    private static void showAllTransports(List<Transport> transports) {
        System.out.println("\n=== Весь транспорт ===");
        if (transports.isEmpty()) {
            System.out.println("Транспортные средства отсутствуют");
            return;
        }
        
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
        
        try {
            System.out.println("\nВыберите транспорт:");
            for (int i = 0; i < transports.size(); i++) {
                System.out.println((i + 1) + ". " + transports.get(i).getName());
            }
            
            int index = readIntInput(scanner, 1, transports.size()) - 1;
            Transport transport = transports.get(index);
            
            System.out.println("\nУправление: " + transport.getName());
            System.out.println("1. Двигаться");
            System.out.println("2. Остановиться");
            System.out.println("3. Информация");
            
            int maxAction = 3;
            if (transport instanceof Car) {
                System.out.println("4. Подать сигнал");
                maxAction = 4;
            } else if (transport instanceof Bicycle) {
                System.out.println("4. Позвонить в звонок");
                maxAction = 4;
            } else if (transport instanceof Airplane) {
                System.out.println("4. Взлететь");
                System.out.println("5. Приземлиться");
                maxAction = 5;
            } else if (transport instanceof Ship) {
                System.out.println("4. Подать гудок");
                maxAction = 4;
            }
            
            int action = readIntInput(scanner, 1, maxAction);
            
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
            }
        } catch (Exception e) {
            System.out.println("Ошибка при управлении транспортом: " + e.getMessage());
        }
    }
    
    private static void addNewTransport(List<Transport> transports, Scanner scanner) {
        try {
            System.out.println("\n=== Добавление нового транспорта ===");
            System.out.println("1. Автомобиль");
            System.out.println("2. Велосипед");
            System.out.println("3. Самолет");
            System.out.println("4. Корабль");
            System.out.print("Выберите тип: ");
            
            int type = readIntInput(scanner, 1, 4);
            scanner.nextLine(); // consume newline
            
            System.out.print("Введите название: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Название не может быть пустым");
            }
            
            System.out.print("Введите максимальную скорость: ");
            int maxSpeed = readIntInput(scanner, 1, 2000);
            
            switch (type) {
                case 1:
                    System.out.print("Введите количество дверей: ");
                    int doors = readIntInput(scanner, 1, 10);
                    System.out.print("Тип топлива (1-Бензин, 2-Дизель, 3-Электричество): ");
                    FuelType fuelType = getFuelType(readIntInput(scanner, 1, 3));
                    Engine carEngine = createEngine(scanner, fuelType);
                    transports.add(new Car(name, maxSpeed, doors, fuelType, carEngine));
                    break;
                case 2:
                    System.out.print("Введите количество передач: ");
                    int gears = readIntInput(scanner, 1, 30);
                    transports.add(new Bicycle(name, maxSpeed, gears));
                    break;
                case 3:
                    System.out.print("Введите размах крыльев: ");
                    int wingspan = readIntInput(scanner, 1, 100);
                    System.out.print("Введите максимальную высоту: ");
                    int altitude = readIntInput(scanner, 1, 50000);
                    Engine planeEngine = new JetEngine("JetEngine-" + name, 10000);
                    transports.add(new Airplane(name, maxSpeed, wingspan, altitude, planeEngine));
                    break;
                case 4:
                    System.out.print("Введите водоизмещение: ");
                    int displacement = readIntInput(scanner, 1, 1000000);
                    System.out.print("Тип топлива (1-Бензин, 2-Дизель): ");
                    FuelType shipFuel = getFuelType(readIntInput(scanner, 1, 2));
                    Engine shipEngine = createEngine(scanner, shipFuel);
                    transports.add(new Ship(name, maxSpeed, displacement, shipFuel, shipEngine));
                    break;
            }
            
            System.out.println("Транспорт добавлен!");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при добавлении транспорта: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
    
    private static FuelType getFuelType(int choice) {
        return switch (choice) {
            case 1 -> FuelType.PETROL;
            case 2 -> FuelType.DIESEL;
            case 3 -> FuelType.ELECTRICITY;
            default -> throw new IllegalArgumentException("Неверный тип топлива");
        };
    }
    
    private static Engine createEngine(Scanner scanner, FuelType fuelType) {
        try {
            scanner.nextLine(); // consume newline
            System.out.print("Введите модель двигателя: ");
            String model = scanner.nextLine().trim();
            if (model.isEmpty()) {
                throw new IllegalArgumentException("Модель двигателя не может быть пустой");
            }
            
            System.out.print("Введите мощность: ");
            double power = readDoubleInput(scanner, 0.1, 100000);
            
            return switch (fuelType) {
                case ELECTRICITY -> {
                    System.out.print("Введите емкость батареи: ");
                    int batteryCapacity = readIntInput(scanner, 1, 10000);
                    yield new ElectricEngine(model, power, batteryCapacity);
                }
                default -> new CombustionEngine(model, power, fuelType);
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании двигателя: " + e.getMessage());
        }
    }
    
    // Вспомогательные методы для безопасного ввода
    private static int readIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = scanner.nextInt();
                if (value < min || value > max) {
                    System.out.printf("Введите число от %d до %d: ", min, max);
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                System.out.printf("Ошибка ввода! Введите целое число от %d до %d: ", min, max);
                scanner.nextLine(); // Очистка буфера
            }
        }
    }
    
    private static double readDoubleInput(Scanner scanner, double min, double max) {
        while (true) {
            try {
                double value = scanner.nextDouble();
                if (value < min || value > max) {
                    System.out.printf("Введите число от %.1f до %.1f: ", min, max);
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                System.out.printf("Ошибка ввода! Введите число от %.1f до %.1f: ", min, max);
                scanner.nextLine(); // Очистка буфера
            }
        }
    }
}