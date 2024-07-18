package com.example.rewardservice.shop.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.category.CategoryRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE category SET status = 'DELETED' WHERE id = ?")
@Where(clause = "status = 'USABLE'")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    public static Category of(final CategoryRequest categoryRequest) {
        return new Category(categoryRequest.getId(), categoryRequest.getName());
    }


    @Override
    public boolean equals(final Object o) {

        if(this==o) {
            return true;
        }
        if(!(o instanceof Category category)){
            return false;
        }
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
