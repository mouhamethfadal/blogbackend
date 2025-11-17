package io.github.mouhamethfadal.blogbackend.entities;

import lombok.Generated;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * Base auditable entity that provides automatic audit tracking for entities.
 * This class is designed to be extended by other entities to automatically
 * capture creation and modification timestamps and user information.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code createdAt} - The timestamp when the entity was created. Automatically populated by Spring Data.</li>
 *   <li>{@code updatedAt} - The timestamp when the entity was last modified. Automatically updated by Spring Data.</li>
 *   <li>{@code createdBy} - The username of the user who created the entity. Automatically populated by Spring Data.</li>
 *   <li>{@code updatedBy} - The username of the user who last modified the entity. Automatically updated by Spring Data.</li>
 * </ul>
 */
@Generated
public class BaseAuditableEntity {
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
}
