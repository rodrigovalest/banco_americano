package com.rodrigovalest.ms_costumer.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

public class NameGeneratorTest {

    @Test
    public void nameGeneratorConstructor_ShouldInstantiate() {
        NameGenerator sut = new NameGenerator();
        Assertions.assertThat(sut).isNotNull();
    }

    @Test
    public void generate_WithValidData_ReturnNewGeneratedName() {
        UUID mockUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        try ( MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(mockUUID);

            // Act
            String generatedName = NameGenerator.generate();

            // Assert
            Assertions.assertThat(generatedName).isNotNull();
            Assertions.assertThat(generatedName).contains("__" + mockUUID.toString());
        }
    }
}
