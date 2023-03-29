import com.nixiedroid.sowftwareId.AbstractGenerator;
import com.nixiedroid.sowftwareId.GetGenerator;
import com.nixiedroid.util.UUID;

import java.util.Arrays;

public class DynamicGeneratorTest {
    public static void main(String[] args) {
        GetGenerator get = new GetGenerator();
        AbstractGenerator gen = get.get();
        System.out.println("" + Arrays.toString(gen.getSoftwareID(UUID.cnv("1234567812345678"), 4)));
    }
}
