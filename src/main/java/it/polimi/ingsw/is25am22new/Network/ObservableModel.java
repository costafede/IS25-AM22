package it.polimi.ingsw.is25am22new.Network;

public class ObservableModel {
    List<VirtualServer> listeners = new ArrayList<>();

    public void addListener(VirtualServer_DataListener ld) {
        listeners.add(ld);
    }
    public void removeListener(VirtualServer_DataListener ld) {
        listeners.remove(ld);
    }
    protected void updateAllStates(int state) throws RemoteException {
        for(VirtualServer_DataListener ld : listeners)
            ld.update(state);
    }
}
