package backend.airo.batch.utils;

import backend.airo.cache.vo.AreaName;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingAreaCode {

    private static final Pattern MEGA_PATTERN = Pattern.compile(
            "^(서울특별시|부산광역시|대구광역시|인천광역시|광주광역시|대전광역시|울산광역시|세종특별자치시|"
                    + "경기도|강원특별자치도|강원도|충청북도|충청남도|전라북도|전라남도|경상북도|경상남도|"
                    + "제주특별자치도|제주도|전북특별자치도)"
    );

    private static final Pattern SGG_TOKEN = Pattern.compile(
            "^(?:[가-힣·]{2,}(?<!북부|남부|동부|서부|중부)시|[가-힣·]{2,}군|[가-힣·]{2,}구|[동서남북중]구)$"
    );

    public static AreaName areaNameParsing(String roadAddr, String lotAddr) {

        String addr = (roadAddr != null && !roadAddr.isBlank()) ? roadAddr : lotAddr;
        if (addr == null || addr.isBlank()) return new AreaName("UNKNOWN", "UNKNOWN");
        addr = addr.trim();

        Matcher mm = MEGA_PATTERN.matcher(addr);
        if (!mm.find()) return new AreaName("UNKNOWN", "UNKNOWN");

        String mega = normalizeMega(mm.group(1));
        String rest = addr.substring(mm.end());

        if ("세종특별자치시".equals(mega)) {
            return new AreaName(mega, "세종특별자치시");
        }

        String cleaned = rest
                .replaceAll("\\([^)]*\\)", " ")
                .replaceAll("[,]", " ")
                .trim();

        List<String> picked = new ArrayList<>(2);
        if (!cleaned.isEmpty()) {
            for (String token : cleaned.split("\\s+")) {
                if (SGG_TOKEN.matcher(token).matches()) {
                    if (picked.isEmpty() || !picked.get(picked.size() - 1).equals(token)) {
                        picked.add(token);
                        if (picked.size() == 2) break;
                    }
                }
            }
        }

        if (picked.isEmpty()) {
            Matcher fallback = Pattern.compile("([가-힣·]{2,}시)([가-힣·]{2,}구)").matcher(cleaned);
            if (fallback.find()) {
                picked.add(fallback.group(1));
                picked.add(fallback.group(2));
            } else {
                Matcher onlySi = Pattern.compile("([가-힣·]{2,}시)").matcher(cleaned);
                if (onlySi.find()) picked.add(onlySi.group(1));
                Matcher onlyGun = Pattern.compile("([가-힣·]{2,}군)").matcher(cleaned);
                if (picked.isEmpty() && onlyGun.find()) picked.add(onlyGun.group(1));
                Matcher onlyGu = Pattern.compile("([가-힣·]{2,}구|[동서남북중]구)").matcher(cleaned);
                if (picked.isEmpty() && onlyGu.find()) picked.add(onlyGu.group(1));
            }
        }

        String city = String.join(" ", picked);
        city = normalizeCity(mega, city);

        if (city.isBlank()) city = "UNKNOWN";
        return new AreaName(mega, city);
    }

    private static String normalizeMega(String mega) {
        if ("강원도".equals(mega)) return "강원특별자치도";
        if ("제주도".equals(mega)) return "제주특별자치도";
        return mega;
    }

    private static String normalizeCity(String mega, String city) {
        if (city == null) return "";
        if ("인천광역시".equals(mega) && "남구".equals(city.trim())) return "미추홀구";
        return city.trim().replaceAll("\\s+", " ");
    }
}
