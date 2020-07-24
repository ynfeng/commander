package com.github.ynfeng.commander.core.definition;

import java.util.List;

public class RelationShips {
    private final List<Link> normalLinks;

    public RelationShips(List<Link> normalLinks) {
        this.normalLinks = normalLinks;
    }

    public static RelationShipBuilder builder() {
        return new RelationShipBuilder();
    }

    public List<Link> links() {
        return normalLinks;
    }
}
