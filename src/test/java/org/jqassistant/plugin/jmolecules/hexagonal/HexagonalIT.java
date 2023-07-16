package org.jqassistant.plugin.jmolecules.hexagonal;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import org.jqassistant.plugin.jmolecules.AbstractJMoleculesPluginIT;
import org.jqassistant.plugin.jmolecules.set.hexagonal.HexagonalApp;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HexagonalIT extends AbstractJMoleculesPluginIT {

    @Test
    public void applicationCoreType() throws RuleException {
        scanClassesAndPackages(HexagonalApp.class);
        assertThat(applyConcept("jmolecules-hexagonal:ApplicationCoreType").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> types = query("MATCH (:JMolecules:Architecture:Hexagonal:ApplicationCore)-[:CONTAINS]->(t:Type:Java) RETURN t ORDER BY t.name").getColumn("t");
        assertThat(types.size()).isEqualTo(2);
        assertThat(types.get(0).getName()).isEqualTo("CoreService");
        assertThat(types.get(1).getName()).isEqualTo("package-info");
        store.commitTransaction();
    }

    @Test
    public void applicationCorePackage() throws RuleException {
        scanClassesAndPackages(HexagonalApp.class);
        assertThat(applyConcept("jmolecules-hexagonal:ApplicationCorePackage").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> types = query("MATCH (:JMolecules:Architecture:Hexagonal:ApplicationCore)-[:CONTAINS]->(t:Type) RETURN t ORDER BY toLower(t.name)").getColumn("t");
        assertThat(types.size()).isEqualTo(10);
        assertThat(types.get(0).getName()).isEqualTo("package-info");
        assertThat(types.get(1).getName()).isEqualTo("package-info");
        assertThat(types.get(2).getName()).isEqualTo("package-info");
        assertThat(types.get(3).getName()).isEqualTo("package-info");
        assertThat(types.get(4).getName()).isEqualTo("PortPackage");
        assertThat(types.get(5).getName()).isEqualTo("PortType");
        assertThat(types.get(6).getName()).isEqualTo("PrimaryPortPackage");
        assertThat(types.get(7).getName()).isEqualTo("PrimaryPortType");
        assertThat(types.get(8).getName()).isEqualTo("SecondaryPortPackage");
        assertThat(types.get(9).getName()).isEqualTo("SecondaryPortType");
        store.commitTransaction();
    }

    @Test
    public void portType() throws RuleException {
        scanClassesAndPackages(HexagonalApp.class);
        assertThat(applyConcept("jmolecules-hexagonal:PortType").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        TestResult unqualifiedTypes = query("MATCH (p:JMolecules:Architecture:Hexagonal:Port{type: 'Unqualified'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, p.name AS port ORDER BY toLower(t.name)");
        assertThat(unqualifiedTypes.getRows().size()).isEqualTo(1);
        assertThat(unqualifiedTypes.getRows().get(0).get("type")).isEqualTo("PortType");
        assertThat(unqualifiedTypes.getRows().get(0).get("port")).isEqualTo("PortType");

        TestResult primaryTypes = query("MATCH (p:JMolecules:Architecture:Hexagonal:Port{type: 'Primary'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, p.name AS port ORDER BY toLower(t.name)");
        assertThat(primaryTypes.getRows().size()).isEqualTo(1);
        assertThat(primaryTypes.getRows().get(0).get("type")).isEqualTo("PrimaryPortType");
        assertThat(primaryTypes.getRows().get(0).get("port")).isEqualTo("PrimaryPortType");

        TestResult secondaryTypes = query("MATCH (p:JMolecules:Architecture:Hexagonal:Port{type: 'Secondary'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, p.name AS port ORDER BY toLower(t.name)");
        assertThat(secondaryTypes.getRows().size()).isEqualTo(1);
        assertThat(secondaryTypes.getRows().get(0).get("type")).isEqualTo("SecondaryPortType");
        assertThat(secondaryTypes.getRows().get(0).get("port")).isEqualTo("SecondaryPortType");
        store.commitTransaction();
    }

    @Test
    public void portPackage() throws RuleException {
        scanClassesAndPackages(HexagonalApp.class);
        assertThat(applyConcept("jmolecules-hexagonal:PortPackage").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        TestResult unqualifiedTypes = query("MATCH (p:JMolecules:Architecture:Hexagonal:Port{type: 'Unqualified'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, p.name AS port ORDER BY toLower(t.name)");
        assertThat(unqualifiedTypes.getRows().size()).isEqualTo(2);
        assertThat(unqualifiedTypes.getRows().get(0).get("type")).isEqualTo("package-info");
        assertThat(unqualifiedTypes.getRows().get(0).get("port")).isEqualTo("port");
        assertThat(unqualifiedTypes.getRows().get(1).get("type")).isEqualTo("PortPackage");
        assertThat(unqualifiedTypes.getRows().get(1).get("port")).isEqualTo("port");

        TestResult primaryTypes = query("MATCH (p:JMolecules:Architecture:Hexagonal:Port{type: 'Primary'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, p.name AS port ORDER BY toLower(t.name)");
        assertThat(primaryTypes.getRows().size()).isEqualTo(2);
        assertThat(primaryTypes.getRows().get(0).get("type")).isEqualTo("package-info");
        assertThat(primaryTypes.getRows().get(0).get("port")).isEqualTo("primaryport");
        assertThat(primaryTypes.getRows().get(1).get("type")).isEqualTo("PrimaryPortPackage");
        assertThat(primaryTypes.getRows().get(1).get("port")).isEqualTo("primaryport");

        TestResult secondaryTypes = query("MATCH (p:JMolecules:Architecture:Hexagonal:Port{type: 'Secondary'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, p.name AS port ORDER BY toLower(t.name)");
        assertThat(secondaryTypes.getRows().size()).isEqualTo(2);
        assertThat(secondaryTypes.getRows().get(0).get("type")).isEqualTo("package-info");
        assertThat(secondaryTypes.getRows().get(0).get("port")).isEqualTo("secondaryport");
        assertThat(secondaryTypes.getRows().get(1).get("type")).isEqualTo("SecondaryPortPackage");
        assertThat(secondaryTypes.getRows().get(1).get("port")).isEqualTo("secondaryport");
        store.commitTransaction();
    }

    @Test
    public void adapterType() throws RuleException {
        scanClassesAndPackages(HexagonalApp.class);
        assertThat(applyConcept("jmolecules-hexagonal:AdapterType").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        TestResult unqualifiedTypes = query("MATCH (a:JMolecules:Architecture:Hexagonal:Adapter{type: 'Unqualified'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, a.name AS adapter ORDER BY toLower(t.name)");
        assertThat(unqualifiedTypes.getRows().size()).isEqualTo(1);
        assertThat(unqualifiedTypes.getRows().get(0).get("type")).isEqualTo("AdapterType");
        assertThat(unqualifiedTypes.getRows().get(0).get("adapter")).isEqualTo("AdapterType");

        TestResult primaryTypes = query("MATCH (a:JMolecules:Architecture:Hexagonal:Adapter{type: 'Primary'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, a.name AS adapter ORDER BY toLower(t.name)");
        assertThat(primaryTypes.getRows().size()).isEqualTo(1);
        assertThat(primaryTypes.getRows().get(0).get("type")).isEqualTo("PrimaryAdapterType");
        assertThat(primaryTypes.getRows().get(0).get("adapter")).isEqualTo("PrimaryAdapterType");

        TestResult secondaryTypes = query("MATCH (a:JMolecules:Architecture:Hexagonal:Adapter{type: 'Secondary'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, a.name AS adapter ORDER BY toLower(t.name)");
        assertThat(secondaryTypes.getRows().size()).isEqualTo(1);
        assertThat(secondaryTypes.getRows().get(0).get("type")).isEqualTo("SecondaryAdapterType");
        assertThat(secondaryTypes.getRows().get(0).get("adapter")).isEqualTo("SecondaryAdapterType");
        store.commitTransaction();
    }

    @Test
    public void adapterPackage() throws RuleException {
        scanClassesAndPackages(HexagonalApp.class);
        assertThat(applyConcept("jmolecules-hexagonal:AdapterPackage").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        TestResult unqualifiedTypes = query("MATCH (a:JMolecules:Architecture:Hexagonal:Adapter{type: 'Unqualified'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, a.name AS adapter ORDER BY toLower(t.name)");
        assertThat(unqualifiedTypes.getRows().size()).isEqualTo(2);
        assertThat(unqualifiedTypes.getRows().get(0).get("type")).isEqualTo("AdapterPackage");
        assertThat(unqualifiedTypes.getRows().get(0).get("adapter")).isEqualTo("adapter");
        assertThat(unqualifiedTypes.getRows().get(1).get("type")).isEqualTo("package-info");
        assertThat(unqualifiedTypes.getRows().get(1).get("adapter")).isEqualTo("adapter");

        TestResult primaryTypes = query("MATCH (a:JMolecules:Architecture:Hexagonal:Adapter{type: 'Primary'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, a.name AS adapter ORDER BY toLower(t.name)");
        assertThat(primaryTypes.getRows().size()).isEqualTo(2);
        assertThat(primaryTypes.getRows().get(0).get("type")).isEqualTo("package-info");
        assertThat(primaryTypes.getRows().get(0).get("adapter")).isEqualTo("primaryadapter");
        assertThat(primaryTypes.getRows().get(1).get("type")).isEqualTo("PrimaryAdapterPackage");
        assertThat(primaryTypes.getRows().get(1).get("adapter")).isEqualTo("primaryadapter");

        TestResult secondaryTypes = query("MATCH (a:JMolecules:Architecture:Hexagonal:Adapter{type: 'Secondary'})-[:IMPLEMENTED_BY]->(t:Type) RETURN t.name AS type, a.name AS adapter ORDER BY toLower(t.name)");
        assertThat(secondaryTypes.getRows().size()).isEqualTo(2);
        assertThat(secondaryTypes.getRows().get(0).get("type")).isEqualTo("package-info");
        assertThat(secondaryTypes.getRows().get(0).get("adapter")).isEqualTo("secondaryadapter");
        assertThat(secondaryTypes.getRows().get(1).get("type")).isEqualTo("SecondaryAdapterPackage");
        assertThat(secondaryTypes.getRows().get(1).get("adapter")).isEqualTo("secondaryadapter");
        store.commitTransaction();
    }

    @Test
    public void aggregateDependencies() {

    }

}