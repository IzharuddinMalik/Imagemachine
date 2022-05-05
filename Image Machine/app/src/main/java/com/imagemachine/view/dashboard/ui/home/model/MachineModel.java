package com.imagemachine.view.dashboard.ui.home.model;

public class MachineModel {

    private String idMachine;
    private String nameMachine;
    private String typeMachine;
    private String qrCodeMachine;
    private String lastMaintMachine;

    public MachineModel(String idMachine, String nameMachine, String typeMachine, String qrCodeMachine, String lastMaintMachine) {
        this.idMachine = idMachine;
        this.nameMachine = nameMachine;
        this.typeMachine = typeMachine;
        this.qrCodeMachine = qrCodeMachine;
        this.lastMaintMachine = lastMaintMachine;
    }

    public MachineModel() {

    }

    public String getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(String idMachine) {
        this.idMachine = idMachine;
    }

    public String getNameMachine() {
        return nameMachine;
    }

    public void setNameMachine(String nameMachine) {
        this.nameMachine = nameMachine;
    }

    public String getTypeMachine() {
        return typeMachine;
    }

    public void setTypeMachine(String typeMachine) {
        this.typeMachine = typeMachine;
    }

    public String getQrCodeMachine() {
        return qrCodeMachine;
    }

    public void setQrCodeMachine(String qrCodeMachine) {
        this.qrCodeMachine = qrCodeMachine;
    }

    public String getLastMaintMachine() {
        return lastMaintMachine;
    }

    public void setLastMaintMachine(String lastMaintMachine) {
        this.lastMaintMachine = lastMaintMachine;
    }
}
