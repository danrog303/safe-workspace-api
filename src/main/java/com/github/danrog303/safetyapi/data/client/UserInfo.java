package com.github.danrog303.safetyapi.data.client;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.github.danrog303.safetyapi.data.equipment.SafetyEquipment;
import com.github.danrog303.safetyapi.data.equipment.SafetyEquipmentListMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the user of the application. The user can be either physical
 * (the actual manager or administrator of the company's IT system) or logical
 * (the client can be a computer that photographs the workplace and checks that all people have the
 * necessary protective equipment).
 */
@Data @AllArgsConstructor @NoArgsConstructor
@DynamoDBTable(tableName="safety-api-user-info")
public class UserInfo implements UserDetails {
    /**
     * User's login.
     */
    @DynamoDBHashKey
    private String login;

    /**
     * User's password.
     */
    @DynamoDBAttribute
    private String password;

    /**
     * User's display name.
     */
    @DynamoDBAttribute
    private String displayName;

    /**
     * Stores permissions of the user (what actions can this client do?)
     */
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = UserAuthorityListMapper.class)
    private List<UserAuthority> permissions;

    /**
     * Last time this user has performed an API requests.
     * This field is used primarily for thresholding image upload.
     */
    @DynamoDBAttribute
    private Date lastAction;

    /**
     * When this client uploads some photos, what is the required protective equipment every
     * person on the photo has to have?
     */
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = SafetyEquipmentListMapper.class)
    private List<SafetyEquipment> requiredEquipment;

    /**
     * <p>Counter increased when this user uploads a photo with a person that does not have {@link #requiredEquipment}
     * on them. Counter goes back to 0 when there are no people on the photo or when all of them have
     * the required protective equipment.</p>
     *
     * <p>When this counter equals the value specified in the application.properties file, a warning email
     * will be sent to all emails specified in the {@link #reportTo} field.</p>
     */
    @DynamoDBAttribute
    private int requiredEquipmentFailureCount;

    /**
     * A list of emails to which information about a security breach by this user will be sent.
     */
    @DynamoDBAttribute
    private List<String> reportTo;

    @DynamoDBIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @DynamoDBIgnore
    public String getUsername() {
        return login;
    }

    @Override
    @DynamoDBIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @DynamoDBIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @DynamoDBIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @DynamoDBIgnore
    public boolean isEnabled() {
        return true;
    }
}
