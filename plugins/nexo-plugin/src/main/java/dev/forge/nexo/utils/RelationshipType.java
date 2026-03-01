package dev.forge.nexo.utils;

public enum RelationshipType {
    ONE_TO_MANY,
    MANY_TO_ONE,
    ONE_TO_ONE,
    MANY_TO_MANY;

    public boolean inverseOf(RelationshipType target) {
        return target == getInverse();
    }

    public RelationshipType getInverse() {
        return switch (this) {
            case MANY_TO_MANY -> RelationshipType.MANY_TO_MANY;
            case MANY_TO_ONE -> RelationshipType.ONE_TO_MANY;
            case ONE_TO_MANY -> RelationshipType.MANY_TO_ONE;
            case ONE_TO_ONE -> RelationshipType.ONE_TO_ONE;
        };
    }

    public String getAnnotationName() {
        return switch (this) {
            case MANY_TO_MANY -> "ManyToMany";
            case MANY_TO_ONE -> "ManyToOne";
            case ONE_TO_MANY -> "OneToMany";
            case ONE_TO_ONE -> "OneToOne";
        };
    }
}
