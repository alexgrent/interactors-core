package org.reactome.server.tools.interactors.model.psicquic;

import org.reactome.server.tools.interactors.model.InteractionDetails;
import org.reactome.server.tools.interactors.model.Interactor;
import psidev.psi.mi.tab.model.Confidence;
import psidev.psi.mi.tab.model.CrossReference;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Guilherme S Viteri <gviteri@ebi.ac.uk>
 */

public abstract class AbstractClient implements PsicquicClient {

    enum InteractorLink {
        A,
        B
    }

    private String resource;

    public AbstractClient(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Interactor getInteractor(EncoreInteraction encoreInteraction, InteractorLink link) {
        Interactor interactor = new Interactor();

        switch (link){
            case A:
                interactor.setAcc(encoreInteraction.getInteractorA());
                interactor.setAlias(getAlias(encoreInteraction.getOtherInteractorAccsA()));
                break;
            case B:
                interactor.setAcc(encoreInteraction.getInteractorB());
                interactor.setAlias(getAlias(encoreInteraction.getOtherInteractorAccsB()));
                break;
        }

        return interactor;
    }

    /**
     * Get miscore which is calculate by InteractionClusterScore service
     * @return miscore
     */
    public double getMiscore(List<Confidence> confidenceValues) {
        String miscore = "";
        for (Confidence confidence : confidenceValues) {
            String type = confidence.getType();
            if (type.equalsIgnoreCase("miscore")) {
                miscore = confidence.getValue();
                break;
            }
        }

        return new Double(miscore);
    }

    /**
     * Retrieve the most appropriate alias from a List of Aliases.
     * The rule is:
     * First: UniProtKB
     * Second: psi-mi
     * Third: The first instance
     *
     * @param accessions key=dbsource{psi-mi,uniprot,etc} value=alias
     */
    public String getAlias(Map<String, List<String>> accessions) {
        String uniprotAlias = "";
        String psimiAlias = "";
        String otherAlias = "";

        for (String dbSource : accessions.keySet()) {
            if (dbSource.equalsIgnoreCase("uniprotkb") && uniprotAlias.isEmpty()) {
                uniprotAlias = accessions.get(dbSource).get(0);
            } else if (dbSource.equalsIgnoreCase("psi-mi") && psimiAlias.isEmpty()) {
                psimiAlias = accessions.get(dbSource).get(0);
            } else if (otherAlias.isEmpty()) {
                otherAlias = accessions.get(dbSource).get(0);
            }
        }

        if (!uniprotAlias.isEmpty()) return uniprotAlias;

        if (!psimiAlias.isEmpty()) return psimiAlias;

        return otherAlias;
    }

    /**
     * Get interaction identifier
     * @param interactionAcs key=interactionId, value=dbSource
     */
    public List<InteractionDetails> getInteractionAc(Map<String, List<String>> interactionAcs) {
        List<InteractionDetails> interactionDetailsList = new ArrayList<>();
        for (String interactionId : interactionAcs.keySet()) {
            InteractionDetails interactionDetails = new InteractionDetails();
            interactionDetails.setInteractionAc(interactionId);
            interactionDetailsList.add(interactionDetails);
        }

        return interactionDetailsList;
    }


}