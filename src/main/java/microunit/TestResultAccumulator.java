package microunit;

import java.lang.reflect.Method;

/**
 * Represents an object accumulating the results of test method executions.
 */
public interface TestResultAccumulator {

    /**
     * Invoked when the execution of a test method results in an error.
     *
     * @param method the method whose execution results in an error
     */
    void onError(Method method);

    /**
     * Invoked when the execution of a test method results in a failure.
     *
     * @param method the method whose execution results in a failure
     */
    void onFailure(Method method);

    /**
     * Invoked when a test method is successfully executed.
     *
     * @param method the method that is executed successfully
     */
    void onSuccess(Method method);

}
