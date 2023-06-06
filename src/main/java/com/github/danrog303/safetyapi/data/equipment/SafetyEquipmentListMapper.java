package com.github.danrog303.safetyapi.data.equipment;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.ArrayList;
import java.util.List;

public class SafetyEquipmentListMapper implements DynamoDBTypeConverter<List<String>, List<SafetyEquipment>> {
    @Override
    public List<String> convert(List<SafetyEquipment> object) {
        List<String> result = new ArrayList<>();
        if (object != null) {
            object.forEach(e -> result.add(e.name()));
        }
        return result;
    }

    @Override
    public List<SafetyEquipment> unconvert(List<String> object) {
        List<SafetyEquipment> result = new ArrayList<>();
        if (object != null) {
            object.forEach(e -> result.add(SafetyEquipment.valueOf(e)));
        }
        return result;
    }
}
