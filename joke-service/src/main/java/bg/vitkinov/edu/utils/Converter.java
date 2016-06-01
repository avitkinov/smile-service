package bg.vitkinov.edu.utils;

public interface Converter<F, T> {

	T convert(F value);
}
