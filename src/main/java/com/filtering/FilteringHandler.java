package com.filtering;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.TreeMap;

/**
 * Realization filtering
 *
 * @author Nicolas Asinovich.
 */
class FilteringHandler extends DefaultHandler {
    /**
     * The TreeMap will store our rules, where attribute of rule the name is unique.
     */
    private TreeMap<String, Rule> dataRule = new TreeMap<>();

    TreeMap<String, Rule> getDataRule () {
        return dataRule;
    }

    @Override
    public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("rule")) {
            getFilteringRule(attributes);
        }
    }

    /**
     * Made logic filtering
     * @param attributes
     */
    // TODO: 13.07.2016 CHECK THIS
    public void getFilteringRule (Attributes attributes) {
        Rule ruleName = dataRule.get(attributes.getValue("name"));
        RuleType type = RuleType.valueOf(attributes.getValue("type").toUpperCase());
        int weight = Integer.parseInt(attributes.getValue("weight"));

        /* If element hasn't key 'name', we will add it. */
        if (!dataRule.containsKey(attributes.getValue("name"))) {
            dataRule.put(attributes.getValue("name"), new Rule(type, weight));
        } else {
            /* If elements have some types, we will compare them by weight. */
            if (type.getRuleTypePrecedence() == ruleName.getRuleType().getRuleTypePrecedence()) {
                if (weight > ruleName.getWeight()) {
                    ruleName.setWeight(weight);
                }
            }
            else if (type.getRuleTypePrecedence() < ruleName.getRuleType().getRuleTypePrecedence()){
            /* If elements have some keys, we will compare them by type. */
//                    addRule(attributes);
                ruleName.setRuleType(type);
            }
        }
    }
}
