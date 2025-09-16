package backend.airo.domain.clure_fatvl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNormalizer {
    private static final Pattern BR = Pattern.compile("(?i)<br\\s*/?>");
    private static final Pattern PHONE = Pattern.compile("\\+?\\d[\\d\\s\\-()]{6,}\\d");

    public static List<String> allPhones(String raw) {
        if (raw == null) return List.of();
        String s = BR.matcher(raw).replaceAll(",");
        Matcher m = PHONE.matcher(s);
        List<String> out = new ArrayList<>();
        while (m.find()) {
            String local = toLocalDigits(m.group());
            if (local != null) out.add(local);
        }
        return out.stream().distinct().limit(5).toList();
    }

    private static String toLocalDigits(String token) {
        if (token == null) return null;
        String digits = token.replaceAll("\\D", "");  // 숫자만

        if (digits.startsWith("82") && !digits.startsWith("820")) digits = "0" + digits.substring(2);
        if (digits.startsWith("820")) digits = "0" + digits.substring(3);

        if (!digits.startsWith("0")) return null;        // 국내형만 허용
        if (digits.length() < 8 || digits.length() > 13) return null; // 대략적 길이 필터
        return digits;
    }


}