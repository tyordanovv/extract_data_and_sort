package entities;

public class Data {
    private String logFileData;
    private String sortedOutputData;

    public String getLogFileData() {
        return logFileData;
    }

    public void setLogFileData(String logFileData) {
        this.logFileData += logFileData;
    }

    public String getSortedOutputData() {
        return sortedOutputData;
    }

    public void setSortedOutputData(String sortedOutputData) {
        this.sortedOutputData += sortedOutputData;
    }

    public Data(){
        this.logFileData = "";
        this.sortedOutputData = "";
    }
}