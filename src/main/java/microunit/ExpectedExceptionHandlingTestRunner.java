package microunit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  Class for running unit tests with support for expected exceptions.
 */
public class ExpectedExceptionHandlingTestRunner extends TestRunner {

    /**
     * Creates a {@code ExpectedExceptionHandlingTestRunner} object for
     * executing the test methods of the class specified.
     *
     * @param testClass the class whose test methods will be executed
     */
    public ExpectedExceptionHandlingTestRunner(Class<?> testClass) {
        super(testClass);
    }

    @Override
    protected void invokeTestMethod(Method testMethod, Object instance, TestResultAccumulator accumulator)
            throws IllegalAccessException {
        Test testAnnotation = testMethod.getAnnotation(Test.class);
        try {
            testMethod.invoke(instance);
            if (! testAnnotation.expected().equals(Test.None.class)) {
                // exception is not thrown
                throw new InvocationTargetException(new AssertionError(testAnnotation.expected().getName() + " is not thrown"));
            }
            accumulator.onSuccess(testMethod);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            cause.printStackTrace(System.out);
            if (cause instanceof AssertionError) {
                accumulator.onFailure(testMethod);
            } else {
                if (testAnnotation.expected().equals(Test.None.class)) {
                    accumulator.onError(testMethod);
                } else {
                    if (cause.getClass().equals(testAnnotation.expected())) {
                        accumulator.onSuccess(testMethod);
                    } else {
                        accumulator.onError(testMethod);
                    }
                }
            }
        }
    }

    // CHECKSTYLE:OFF
    public static void main(String[] args) throws Exception {
        Class<?> testClass = Class.forName(args[0]);
        new ExpectedExceptionHandlingTestRunner(testClass).runTestMethods();
    }

}
