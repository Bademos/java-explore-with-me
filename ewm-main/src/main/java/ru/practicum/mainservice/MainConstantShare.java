package ru.practicum.mainservice;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class MainConstantShare {
    public final Sort sortDesc = Sort.by(Sort.Direction.DESC, "id");

}
