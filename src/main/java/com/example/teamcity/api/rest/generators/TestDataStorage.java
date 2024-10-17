package com.example.teamcity.api.rest.generators;

import com.example.teamcity.api.rest.enums.Endpoint;
import com.example.teamcity.api.rest.models.BaseModel;
import com.example.teamcity.api.rest.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.rest.spec.Specifications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TestDataStorage {

    private static TestDataStorage instance;
    private final EnumMap<Endpoint, Set<String>> createdEntitiesMap;

    private TestDataStorage() {
        createdEntitiesMap = new EnumMap<>(Endpoint.class);
    }

    public static TestDataStorage getInstance() {
        if (instance == null) {
            instance = new TestDataStorage();
        }
        return instance;
    }

    private void addCreatedEntity(Endpoint endpoint, String id) {
        if (id != null) {
            createdEntitiesMap.computeIfAbsent(endpoint, key -> new LinkedHashSet<>()).add(id);
        }
    }

    private String getEntityIdOrLocator(BaseModel model) {
        try {
            var idField = model.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            var idFieldValue = Objects.toString(idField.get(model), null);
            idField.setAccessible(false);
            return idFieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            try {
                var locatorField = model.getClass().getDeclaredField("locator");
                locatorField.setAccessible(true);
                var locatorFieldValue = Objects.toString(locatorField.get(model), null);
                locatorField.setAccessible(false);
                return locatorFieldValue;
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new IllegalStateException("Cannot get id or locator of entity", e);
            }
        }
    }

    public void addCreatedEntity(Endpoint endpoint, BaseModel model) {
        addCreatedEntity(endpoint, getEntityIdOrLocator(model));
    }

    public void deleteCreatedEntities() {
        List<Endpoint> reverseOrderEndpoints = new ArrayList<>(createdEntitiesMap.keySet());
        Collections.reverse(reverseOrderEndpoints);

        reverseOrderEndpoints.forEach(endpoint -> {
            LinkedHashSet<String> ids = (LinkedHashSet<String>) createdEntitiesMap.get(endpoint);
            List<String> reverseOrderIds = new ArrayList<>(ids);
            Collections.reverse(reverseOrderIds);

            reverseOrderIds.forEach(id -> new UncheckedBase(Specifications.superUserSpec(), endpoint).delete("id:" + id));
        });

        createdEntitiesMap.clear();
    }

}
