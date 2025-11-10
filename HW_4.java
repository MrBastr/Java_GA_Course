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
        if (str == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
        this.stringBuilder = new StringBuilder(str);
        this.observers = new ArrayList<>();
    }
    
    public ObservableStringBuilder(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Емкость не может быть отрицательной: " + capacity);
        }
        this.stringBuilder = new StringBuilder(capacity);
        this.observers = new ArrayList<>();
    }
    
    // Методы для работы с наблюдателями
    public void addObserver(StringBuilderObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Наблюдатель не может быть null");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void removeObserver(StringBuilderObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Наблюдатель не может быть null");
        }
        observers.remove(observer);
    }
    
    // Уведомление всех наблюдателей
    private void notifyObservers(String oldValue, String newValue) {
        for (StringBuilderObserver observer : observers) {
            try {
                observer.onStringChanged(this, oldValue, newValue);
            } catch (Exception e) {
                System.err.println("Ошибка в наблюдателе: " + e.getMessage());
                // Продолжаем уведомлять других наблюдателей несмотря на ошибку
            }
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
        if (sb == null) {
            throw new IllegalArgumentException("StringBuffer не может быть null");
        }
        return append(sb.toString());
    }
    
    public ObservableStringBuilder append(CharSequence s) {
        if (s == null) {
            throw new IllegalArgumentException("CharSequence не может быть null");
        }
        return append(s.toString());
    }
    
    public ObservableStringBuilder append(CharSequence s, int start, int end) {
        validateCharSequenceBounds(s, start, end);
        String oldValue = stringBuilder.toString();
        stringBuilder.append(s, start, end);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder append(char[] str) {
        if (str == null) {
            throw new IllegalArgumentException("Массив символов не может быть null");
        }
        return append(new String(str));
    }
    
    public ObservableStringBuilder append(char[] str, int offset, int len) {
        validateArrayBounds(str, offset, len);
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
        validateIndex(index, true);
        validateArrayBounds(str, offset, len);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(index, str, offset, len);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, Object obj) {
        validateIndex(offset, true);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, obj);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, String str) {
        validateIndex(offset, true);
        if (str == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, str);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, char[] str) {
        validateIndex(offset, true);
        if (str == null) {
            throw new IllegalArgumentException("Массив символов не может быть null");
        }
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, str);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, CharSequence s) {
        validateIndex(offset, true);
        if (s == null) {
            throw new IllegalArgumentException("CharSequence не может быть null");
        }
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, s);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, CharSequence s, int start, int end) {
        validateIndex(offset, true);
        validateCharSequenceBounds(s, start, end);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, s, start, end);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, boolean b) {
        validateIndex(offset, true);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, b);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, char c) {
        validateIndex(offset, true);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, c);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, int i) {
        validateIndex(offset, true);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, i);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, long l) {
        validateIndex(offset, true);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, l);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, float f) {
        validateIndex(offset, true);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, f);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder insert(int offset, double d) {
        validateIndex(offset, true);
        String oldValue = stringBuilder.toString();
        stringBuilder.insert(offset, d);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder delete(int start, int end) {
        validateRange(start, end);
        String oldValue = stringBuilder.toString();
        stringBuilder.delete(start, end);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder deleteCharAt(int index) {
        validateIndex(index, false);
        String oldValue = stringBuilder.toString();
        stringBuilder.deleteCharAt(index);
        notifyObservers(oldValue, stringBuilder.toString());
        return this;
    }
    
    public ObservableStringBuilder replace(int start, int end, String str) {
        validateRange(start, end);
        if (str == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
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
        if (minimumCapacity < 0) {
            throw new IllegalArgumentException("Минимальная емкость не может быть отрицательной: " + minimumCapacity);
        }
        stringBuilder.ensureCapacity(minimumCapacity);
    }
    
    public void trimToSize() {
        stringBuilder.trimToSize();
    }
    
    public void setLength(int newLength) {
        if (newLength < 0) {
            throw new IllegalArgumentException("Длина не может быть отрицательной: " + newLength);
        }
        String oldValue = stringBuilder.toString();
        stringBuilder.setLength(newLength);
        notifyObservers(oldValue, stringBuilder.toString());
    }
    
    public char charAt(int index) {
        validateIndex(index, false);
        return stringBuilder.charAt(index);
    }
    
    public int codePointAt(int index) {
        validateIndex(index, false);
        return stringBuilder.codePointAt(index);
    }
    
    public int codePointBefore(int index) {
        if (index <= 0 || index > length()) {
            throw new StringIndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        return stringBuilder.codePointBefore(index);
    }
    
    public int codePointCount(int beginIndex, int endIndex) {
        validateRange(beginIndex, endIndex);
        return stringBuilder.codePointCount(beginIndex, endIndex);
    }
    
    public int offsetByCodePoints(int index, int codePointOffset) {
        validateIndex(index, true);
        return stringBuilder.offsetByCodePoints(index, codePointOffset);
    }
    
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        validateRange(srcBegin, srcEnd);
        if (dst == null) {
            throw new IllegalArgumentException("Массив назначения не может быть null");
        }
        if (dstBegin < 0 || dstBegin > dst.length) {
            throw new IllegalArgumentException("Начальный индекс назначения вне диапазона: " + dstBegin);
        }
        if (srcEnd - srcBegin > dst.length - dstBegin) {
            throw new IllegalArgumentException("Недостаточно места в массиве назначения");
        }
        stringBuilder.getChars(srcBegin, srcEnd, dst, dstBegin);
    }
    
    public void setCharAt(int index, char ch) {
        validateIndex(index, false);
        String oldValue = stringBuilder.toString();
        stringBuilder.setCharAt(index, ch);
        notifyObservers(oldValue, stringBuilder.toString());
    }
    
    public String substring(int start) {
        validateIndex(start, true);
        return stringBuilder.substring(start);
    }
    
    public CharSequence subSequence(int start, int end) {
        validateRange(start, end);
        return stringBuilder.subSequence(start, end);
    }
    
    public String substring(int start, int end) {
        validateRange(start, end);
        return stringBuilder.substring(start, end);
    }
    
    @Override
    public String toString() {
        return stringBuilder.toString();
    }
    
    public int indexOf(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
        return stringBuilder.indexOf(str);
    }
    
    public int indexOf(String str, int fromIndex) {
        if (str == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
        return stringBuilder.indexOf(str, fromIndex);
    }
    
    public int lastIndexOf(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
        return stringBuilder.lastIndexOf(str);
    }
    
    public int lastIndexOf(String str, int fromIndex) {
        if (str == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
        return stringBuilder.lastIndexOf(str, fromIndex);
    }
    
    // Вспомогательные методы для валидации
    private void validateIndex(int index, boolean allowLength) {
        int maxIndex = allowLength ? length() : length() - 1;
        if (index < 0 || index > maxIndex) {
            throw new StringIndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
    }
    
    private void validateRange(int start, int end) {
        if (start < 0 || start > end || end > length()) {
            throw new StringIndexOutOfBoundsException(
                "Диапазон вне допустимых значений: start=" + start + ", end=" + end + ", length=" + length());
        }
    }
    
    private void validateArrayBounds(char[] array, int offset, int len) {
        if (array == null) {
            throw new IllegalArgumentException("Массив не может быть null");
        }
        if (offset < 0 || len < 0 || offset + len > array.length) {
            throw new ArrayIndexOutOfBoundsException(
                "Выход за границы массива: offset=" + offset + ", len=" + len + ", array.length=" + array.length);
        }
    }
    
    private void validateCharSequenceBounds(CharSequence s, int start, int end) {
        if (s == null) {
            throw new IllegalArgumentException("CharSequence не может быть null");
        }
        if (start < 0 || end < 0 || start > end || end > s.length()) {
            throw new IndexOutOfBoundsException(
                "Диапазон CharSequence вне допустимых значений: start=" + start + ", end=" + end + ", length=" + s.length());
        }
    }
}

// Демонстрационный класс
public class HW_4 {
    public static void main(String[] args) {
        try {
            // Демонстрация работы с исключениями
            System.out.println("=== Демонстрация обработки исключений ===");
            
            // Тест 1: Создание с некорректной емкостью
            try {
                ObservableStringBuilder invalidBuilder = new ObservableStringBuilder(-5);
            } catch (IllegalArgumentException e) {
                System.out.println("Тест 1 - Ожидаемая ошибка: " + e.getMessage());
            }
            
            // Тест 2: Добавление null
            ObservableStringBuilder builder = new ObservableStringBuilder("Test");
            try {
                builder.append((String) null);
            } catch (IllegalArgumentException e) {
                System.out.println("Тест 2 - Ожидаемая ошибка: " + e.getMessage());
            }
            
            // Тест 3: Некорректный индекс
            try {
                builder.charAt(100);
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Тест 3 - Ожидаемая ошибка: " + e.getMessage());
            }
            
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
            
            // Тест 4: Добавление null наблюдателя
            try {
                observableStringBuilder.addObserver(null);
            } catch (IllegalArgumentException e) {
                System.out.println("Тест 4 - Ожидаемая ошибка: " + e.getMessage());
            }
            
            observableStringBuilder.addObserver(logger);
            observableStringBuilder.addObserver(editor);
            
            // Демонстрация работы
            System.out.println("\n=== Демонстрация ObservableStringBuilder ===");
            
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
            
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка в демонстрации: " + e.getMessage());
            e.printStackTrace();
        }
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