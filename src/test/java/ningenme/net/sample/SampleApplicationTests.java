package ningenme.net.sample;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SampleApplicationTests {

	@Test
	void test() {
		Assertions.assertThat(1).isEqualTo(1);
	}

}
