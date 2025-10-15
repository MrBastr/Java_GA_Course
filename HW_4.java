import java.util.ArrayList;
import java.util.List;

// Интерфейс наблюдателя
interface StringBuilderObserver {
    void onStringChanged(ObservableStringBuilder stringBuilder, String oldValue, String newValue);
}

// Класс ObservableStringBuilder с поддержкой наблюдателей
class ObservableStringBuilder {
    private StringBuilder stringBuilder;
    private List<StringBuilderObserver> observers;
    
    public ObservableStringBuilder() {
        this.stringBuilder = new StringBuilder();
        this.observers = new ArrayList<>();
    }
    
    public ObservableStringBuilder(String str) {
        this.stringBuilder = new StringBuilder(str);
        this.observers = new ArrayList<>();
    }
    
    public ObservableStringBuilder(int capacity) {
        this.stringBuilder = new StringBuilder(capacity);
        this.observers = new ArrayList<>();
    }
    
    // Методы для работы с наблюдателями
    public void addObserver(StringBuilderObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void removeObserver(StringBuilderObserver observer) {
        observers.remove(observer);
    }
    
    // Уведомление всех наблюдателей
    private void notifyObservers(String oldValue, String newValue) {
        for (StringBuilderObserver observer : observers) {
            observer.onStringChanged(this, oldValue, newValue);
        }
    }
    
    // Делегированные методы StringBuilder с уведомлением наблюдателей
    
    public ObservableStringBuilder append(String str) {
        String oldValue = stringBuilder.toString();
        stringBuilder.append(str);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }
    
    public ObservableStringBuilder append(StringBuffer sb) {
        return append(sb.toString());
    }
    
    public ObservableStringBuilder append(CharSequence s) {
        return append(s.toString());
    }
    
    public ObservableStringBuilder append(CharSequence s, int start, int end) {
        String oldValue = stringBuilder.toString();
        stringBuilder.append(s, start, end);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder append(char[] str) {
        return append(new String(str));
    }
    
    public ObservableStringBuilder append(char[] str, int offset, int len) {
        String oldValue = stringBuilder.toString();
        stringBuilder.append(str, offset, len);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder append(boolean b) {
        return append(String.valueOf(b));
    }
    
    public ObservableStringBuilder append(char c) {
        String oldValue = stringBuilder.toString();
        stringBuilder.append(c);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder append(int i) {
        return append(String.valueOf(i));
    }
    
    public ObservableStringBuilder append(long lng) {
        return append(String.valueOf(lng));
    }
    
    public ObservableStringBuilder append(float f) {
        return append(String.valueOf(f));
    }
    
    public ObservableStringBuilder append(double d) {
        return append(String.valueOf(d));
    }
    
    public ObservableStringBuilder insert(int index, char[] str, int offset, int len) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(index, str, offset, len);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, Object obj) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, obj);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, String str) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, str);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, char[] str) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, str);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, CharSequence s) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, s);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, CharSequence s, int start, int end) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, s, start, end);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, boolean b) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, b);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, char c) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, c);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, int i) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, i);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, long l) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, l);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, float f) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, f);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, double d) {
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, d);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder delete(int start, int end) {
        String oldValue = stringBuilder.toString();
        stringBuilder.delete(start, end);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder deleteCharAt(int index) {
        String oldValue = stringBuilder.toString();
        stringBuilder.deleteCharAt(index);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder replace(int start, int end, String str) {
        String oldValue = stringBuilder.toString();
        stringBuilder.replace(start, end, str);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder reverse() {
        String oldValue = stringBuilder.toString();
        stringBuilder.reverse();
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    // Другие методы для получения информации
    
    public int length() {
        return stringBuilder.length();
    }
    
    public int capacity() {
        return stringBuilder.capacity();
    }
    
    public void ensureCapacity(int minimumCapacity) {
        stringBuilder.ensureCapacity(minimumCapacity);
    }
    
    public void trimToSize() {
        stringBuilder.trimToSize();
    }
    
    public void setLength(int newLength) {
        String oldValue = stringBuilder.toString();
        stringBuilder.setLength(newLength);
        notifyObservers(oldValue, stringBuilder.toString());
    }
    
    public char charAt(int index) {
        return stringBuilder.charAt(index);
    }
    
    public int codePointAt(int index) {
        return stringBuilder.codePointAt(index);
    }
    
    public int codePointBefore(int index) {
        return stringBuilder.codePointBefore(index);
    }
    
    public int codePointCount(int beginIndex, int endIndex) {
        return stringBuilder.codePointCount(beginIndex, endIndex);
    }
    
    public int offsetByCodePoints(int index, int codePointOffset) {
        return stringBuilder.offsetByCodePoints(index, codePointOffset);
    }
    
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        stringBuilder.getChars(srcBegin, srcEnd, dst, dstBegin);
    }
    
    public void setCharAt(int index, char ch) {
        String oldValue = stringBuilder.toString();
        stringBuilder.setCharAt(index, ch);
        notifyObservers(oldValue, stringBuilder.toString());
    }
    
    public String substring(int start) {
        return stringBuilder.substring(start);
    }
    
    public CharSequence subSequence(int start, int end) {
        return stringBuilder.subSequence(start, end);
    }
    
    public String substring(int start, int end) {
        return stringBuilder.substring(start, end);
    }
    
    @Override
    public String toString() {
        return stringBuilder.toString();
    }
    
    public int indexOf(String str) {
        return stringBuilder.indexOf(str);
    }
    
    public int indexOf(String str, int fromIndex) {
        return stringBuilder.indexOf(str, fromIndex);
    }
    
    public int lastIndexOf(String str) {
        return stringBuilder.lastIndexOf(str);
    }
    
    public int lastIndexOf(String str, int fromIndex) {
        return stringBuilder.lastIndexOf(str, fromIndex);
    }
}

// Демонстрационный класс
public class HW_4{
    public static void main(String[] args) {
        
        // Создаем наблюдаемый StringBuilder
        ObservableStringBuilder observableStringBuilder = new ObservableStringBuilder("Hello");
        
        // Создаем и регистрируем наблюдателей
        StringBuilderObserver logger = new StringBuilderObserver() {
            @Override
            public void onStringChanged(ObservableStringBuilder stringBuilder, String oldValue, String newValue) {
                System.out.printf("[LOGGER] Изменение строки: '%s' -> '%s' (длина: %d)%n", 
                    oldValue, newValue, newValue.length());
            }
        };
        
        StringBuilderEditor editor = new StringBuilderEditor();
        
        observableStringBuilder.addObserver(logger);
        observableStringBuilder.addObserver(editor);
        
        // Демонстрация работы
        System.out.println("=== Демонстрация ObservableStringBuilder ===");
        
        System.out.println("\n1. Добавление текста:");
        observableStringBuilder.append(" World");
        
        System.out.println("\n2. Вставка текста:");
        observableStringBuilder.insert(5, " Beautiful");
        
        System.out.println("\n3. Замена текста:");
        observableStringBuilder.replace(6, 15, "Amazing");
        
        System.out.println("\n4. Удаление текста:");
        observableStringBuilder.delete(13, 19);
        
        System.out.println("\n5. Добавление чисел и других типов:");
        observableStringBuilder.append(". Number: ").append(42);
        
        System.out.println("\n6. Реверс строки:");
        observableStringBuilder.reverse();
        
        System.out.println("\n7. Установка длины:");
        observableStringBuilder.setLength(10);
        
        // Удаляем одного наблюдателя и показываем, что второй продолжает работать
        System.out.println("\n=== Удаляем логгер и продолжаем работу ===");
        observableStringBuilder.removeObserver(logger);
        
        observableStringBuilder.append(" (без логгера)");
        
        System.out.println("\nИтоговая строка: " + observableStringBuilder.toString());
    }
}

// Пример специализированного наблюдателя
class StringBuilderEditor implements StringBuilderObserver {
    private int changeCount = 0;
    
    @Override
    public void onStringChanged(ObservableStringBuilder stringBuilder, String oldValue, String newValue) {
        changeCount++;
        System.out.printf("[EDITOR #%d] Строка изменена. Старая длина: %d, Новая длина: %d%n", 
            changeCount, oldValue.length(), newValue.length());
        
        // Пример бизнес-логики: если строка становится слишком длинной, выводим предупреждение
        if (newValue.length() > 50) {
            System.out.println("[EDITOR] Внимание: строка превышает 50 символов!");
        }
        
        // Проверка на наличие определенных слов
        if (newValue.toLowerCase().contains("error")) {
            System.out.println("[EDITOR] Обнаружено слово 'error' в строке!");
        }
    }
    
    public int getChangeCount() {
        return changeCount;
    }
}