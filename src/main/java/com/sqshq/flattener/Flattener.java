package com.sqshq.flattener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

import static java.lang.String.format;

/**
 * Converts a hierarchical JSON from InputStream into a flattened version String,
 * with keys as the path to every terminal value in the JSON structure.
 * <p>
 * Assumptions:
 * - the input always fits in RAM
 * - the input JSON does not contain arrays
 *
 * @author Alexander Lukyanchikov
 */
public class Flattener {

    private final ObjectMapper mapper;

    public Flattener(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String flatten(InputStream stream) throws IOException {
        var tree = mapper.readTree(stream);
        if (!tree.isMissingNode()) {
            flatten((ObjectNode) tree);
        }
        return tree.toPrettyString();
    }

    private void flatten(ObjectNode node) {

        var children = node.fields();
        var replacement = new LinkedHashMap<String, JsonNode>();

        while (children.hasNext()) {
            var child = children.next();

            if (child.getValue().isArray()) {
                throw new IllegalArgumentException("Arrays are not supported");
            }

            if (child.getValue().isObject()) {
                flatten((ObjectNode) child.getValue());
                var grandChildren = child.getValue().fields();
                while (grandChildren.hasNext()) {
                    var grandChild = grandChildren.next();
                    replacement.put(format("%s.%s", child.getKey(), grandChild.getKey()), grandChild.getValue());
                }
            } else {
                replacement.put(child.getKey(), child.getValue());
            }

            children.remove();
        }

        node.setAll(replacement);
    }
}
