package com.github.danrog303.safetyapi.data.client;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.ArrayList;
import java.util.List;

public class UserAuthorityListMapper implements DynamoDBTypeConverter<List<String>, List<UserAuthority>> {
    @Override
    public List<String> convert(List<UserAuthority> object) {
        List<String> result = new ArrayList<>();
        if (object != null) {
            object.forEach(e -> result.add(e.name()));
        }
        return result;
    }

    @Override
    public List<UserAuthority> unconvert(List<String> object) {
        List<UserAuthority> result = new ArrayList<>();
        if (object != null) {
            object.forEach(e -> result.add(UserAuthority.valueOf(e)));
        }
        return result;
    }
}
