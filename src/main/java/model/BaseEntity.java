package model;

import java.io.Serializable;

/**
 * This class is a base class for all entity. Extends this class to make use of id for entities.
 *
 * @author Kshitiz Bista
 */
public class BaseEntity implements Serializable {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
