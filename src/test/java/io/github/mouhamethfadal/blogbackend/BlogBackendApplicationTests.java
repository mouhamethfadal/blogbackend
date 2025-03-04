package io.github.mouhamethfadal.blogbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogBackendApplicationTests {
	/**
	 * Verifies that the Spring application context loads successfully.
	 * <p>
	 * This test ensures:
	 * - All required beans can be instantiated and wired correctly
	 * - The embedded MongoDB database starts properly and is accessible
	 * - All MongoDB repositories are initialized correctly
	 * - Configuration properties are valid and properly loaded
	 * <p>
	 * No explicit assertions are needed as the test will fail if any part
	 * of the application context fails to initialize.
	 */
	@Test
	void contextLoads() {
		// Context loading verification happens automatically
		// Test succeeds if application context loads without errors
	}
}
