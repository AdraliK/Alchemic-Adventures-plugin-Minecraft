package helpers.potionColor;


import org.bukkit.Color;
import java.util.HashMap;
import java.util.Map;

public class PotionColorMap {

    public static Map<String, Color> potionColors = new HashMap<>();

    static {
        potionColors.put("NIGHT_VISION", Color.fromARGB(255, 192, 252, 101));     // Ночное зрение
        potionColors.put("INVISIBILITY", Color.fromARGB(255, 243, 243, 243));     // Невидимость
        potionColors.put("JUMP", Color.fromARGB(255, 250, 252, 130));               // Прыгучесть
        potionColors.put("FIRE_RESISTANCE", Color.fromARGB(255, 252, 191, 0));     // Огнестойкость
        potionColors.put("SPEED", Color.fromARGB(255, 50, 232, 252));             // Скорость
        potionColors.put("SLOWNESS", Color.fromARGB(255, 137, 173, 221));         // Замедление
        potionColors.put("TURTLE_MASTER", Color.fromARGB(255, 139, 128, 227));     // Зелье черепахи (сопротивление + замедление)
        potionColors.put("WATER_BREATHING", Color.fromARGB(255, 150, 215, 190));    // Подводное дыхание
        potionColors.put("HEALING", Color.fromARGB(255, 245, 35, 35));            // Мгновенное лечение
        potionColors.put("HARMING", Color.fromARGB(255, 167, 100, 105));              // Мгновенный урон
        potionColors.put("POISON", Color.fromARGB(255, 133, 161, 98));             // Отравление
        potionColors.put("REGENERATION", Color.fromARGB(255, 203, 91, 169));      // Регенерация
        potionColors.put("STRENGTH", Color.fromARGB(255, 252, 197, 0));             // Сила
        potionColors.put("WEAKNESS", Color.fromARGB(255, 71, 76, 71));         // Слабость
        potionColors.put("LUCK", Color.fromARGB(255, 88, 191, 6));                 // Удача
        potionColors.put("SLOW_FALLING", Color.fromARGB(255, 240, 205, 183));     // Медленное падение
        potionColors.put("WIND_CHARGED", Color.fromARGB(255, 187, 199, 252));     // Ветряной заряд
        potionColors.put("WEAVING", Color.fromARGB(255, 119, 104, 89));          // Плетение
        potionColors.put("OOZING", Color.fromARGB(255, 151, 252, 161));              // Слизистость
        potionColors.put("INFESTED", Color.fromARGB(255, 138, 153, 138));         // Зараженное
    }

    public static Color getPotionColor(String potionType) {
        return potionColors.getOrDefault(potionType, Color.WHITE); // По умолчанию возвращаем белый цвет
    }
}

