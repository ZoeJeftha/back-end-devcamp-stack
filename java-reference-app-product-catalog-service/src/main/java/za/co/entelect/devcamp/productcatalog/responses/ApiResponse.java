package za.co.entelect.devcamp.productcatalog.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    public boolean success;
    public String message;
    public T result;
}
