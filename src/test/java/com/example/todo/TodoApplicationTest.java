package com.example.todo;

import org.junit.jupiter.api.Test;

/**
 * A minimal smoke test for the {@link TodoApplication}.  Simply
 * exercising the {@code main} method ensures that the Spring
 * application context can start without throwing exceptions.  This test
 * does not start an embedded server but provides coverage for the
 * entrypoint class.
 */
class TodoApplicationTest {

    @Test
    void applicationMainRunsWithoutError() {
        // The main method should run without throwing any exceptions.
        // We pass an empty argument array as would normally be the case.
        TodoApplication.main(new String[]{});
    }
}