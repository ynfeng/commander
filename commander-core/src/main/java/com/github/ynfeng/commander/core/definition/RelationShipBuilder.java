package com.github.ynfeng.commander.core.definition;

import com.google.common.collect.Lists;
import java.util.List;

public class RelationShipBuilder {
    private final List<Link> links = Lists.newArrayList();

    protected RelationShipBuilder() {
    }

    public RelationShips build() {
        return new RelationShips(links);
    }

    public RelationShipBuilder withLink(String fromRefName, String toRefName) {
        links.add(new NormalLink(fromRefName, toRefName));
        return this;
    }

    public RelationShipBuilder withDecision(String decisionRefName, String exepression, String targetRefName) {
        links.add(new DecisionLink(decisionRefName, exepression, targetRefName));
        return this;
    }

    public RelationShipBuilder withDefaultDecision(String decisionRefName, String targetRefName) {
        links.add(new DefaultDecisionLink(decisionRefName, targetRefName));
        return this;
    }

    public RelationShipBuilder withFork(String forkRefName, String... refNames) {
        links.add(new ForkLink(forkRefName, refNames));
        return this;
    }

    public RelationShipBuilder withJoin(String joinRefName, String... refNames) {
        links.add(new JoinLink(joinRefName, refNames));
        return this;
    }
}
