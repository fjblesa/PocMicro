package crud.crud_core.entities;

import org.junit.Before;
import org.junit.Test;

import crud.crud_core.entities.CommercialSite;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.issues.Issue;

import java.util.List;

import static org.junit.Assert.*;

public class CommercialSitesTest {
    CommercialSite entity;

    @Before
    public void setUp(){
        entity = new CommercialSite();
    }

    @Test
    public void canCloneItself()   {
        WaterUtility dummyEntreprise = EntityTestUtils.createWaterUtilityDummy();

        WaterUtility clonedContract = dummyEntreprise.clone();

        assertNotNull(clonedContract);
        assertEqualEntreprises(dummyEntreprise, clonedContract);
    }

    @Test
    public void canCopyItself()   {
        WaterUtility dummyEntreprise = EntityTestUtils.createWaterUtilityDummy();
        WaterUtility copyEntreprise = new WaterUtility();
        copyEntreprise.copy(dummyEntreprise);

        assertEqualEntreprises(copyEntreprise, dummyEntreprise);
    }

    private List<Issue> whenEntityIsValidated() {
        List<Issue> issues = entity.validate();
        return issues;
    }

    private void assertEqualEntreprises(WaterUtility expected, WaterUtility actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertEquals(expected.getName(), actual.getName());
    }
}