package iuh.fit.se.mapper;

import iuh.fit.se.dtos.request.EmployeeRegistrationRequest;
import iuh.fit.se.dtos.request.EmployeeUpdateRequest;
import iuh.fit.se.dtos.response.EmployeeRegistrationResponse;
import iuh.fit.se.dtos.response.EmployeeUpdateResponse;
import iuh.fit.se.entities.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "roles", ignore = true)
    })
    Employee toEmployee(EmployeeRegistrationRequest request);

    EmployeeRegistrationResponse toEmployeeRegistrationResponse(Employee employee);

    @Mapping(target = "roles", ignore = true)
    Employee toEmployee(EmployeeUpdateRequest request);

    EmployeeUpdateResponse toEmployeeUpdateResponse(Employee employee);

//    default Set<Role> map(List<String> roles) {
//        if (roles == null)
//            return null;
//        return roles.stream()
//                .filter(r -> r != null && !r.isBlank())
//                .map(r -> Role.builder().name(r).build())
//                .collect(Collectors.toSet());
//    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "roles", ignore = true)
    })
    void update(@MappingTarget Employee employee, EmployeeUpdateRequest request);
}
