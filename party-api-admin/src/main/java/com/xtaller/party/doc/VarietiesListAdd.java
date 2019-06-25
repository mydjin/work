package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "品总列表新增")
public class VarietiesListAdd {

    @ApiModelProperty(value = "品种")
    private String name;
    @ApiModelProperty(value = "亲本来源")
    private String source;
    @ApiModelProperty(value = "倍数性")
    private String multiplier;
    @ApiModelProperty(value = "膨芽期")
    private String bentonite;
    @ApiModelProperty(value = "叶长")
    private String leaf_length;
    @ApiModelProperty(value = "叶宽")
    private String leaf_width;
    @ApiModelProperty(value = "叶重")
    private String leaf_weight;
    @ApiModelProperty(value = "育成时间")
    private String breeding_time;
    @ApiModelProperty(value = "树形")
    private String treeType;
    @ApiModelProperty(value = "枝态")
    private String treeState;
    @ApiModelProperty(value = "枝长")
    private String branchLength;
    @ApiModelProperty(value = "枝粗度")
    private String branchThick;
    @ApiModelProperty(value = "皮色")
    private String branchColor;
    @ApiModelProperty(value = "节距(cm)")
    private String branchSpacing;
    @ApiModelProperty(value = "节曲直")
    private String curvature;
    @ApiModelProperty(value = "叶序")
    private String phyllotaxy;
    @ApiModelProperty(value = "皮孔色")
    private String leatherColoring;
    @ApiModelProperty(value = "皮孔密度")
    private String poreDensity;
    @ApiModelProperty(value = "皮孔大小")
    private String holeSize;
    @ApiModelProperty(value = "芽色")
    private String budColor;
    @ApiModelProperty(value = "芽生状")
    private String budShape;
    @ApiModelProperty(value = "副芽")
    private String buds;
    @ApiModelProperty(value = "全叶形")
    private String leafShape;
    @ApiModelProperty(value = "裂叶形")
    private String splitLeafShape;
    @ApiModelProperty(value = "叶色")
    private String leafColor;
    @ApiModelProperty(value = "叶展态")
    private String leafSpreadingState;
    @ApiModelProperty(value = "叶尖")
    private String leafTip;
    @ApiModelProperty(value = "叶缘齿")
    private String leafTooth;
    @ApiModelProperty(value = "叶糙滑")
    private String brownSmooth;
    @ApiModelProperty(value = "叶缩皱")
    private String leafCrinkle;
    @ApiModelProperty(value = "叶面光泽")
    private String foliageLuster;
    @ApiModelProperty(value = "叶着生态")
    private String leafEcology;
    @ApiModelProperty(value = "叶柄长度")
    private String petioleLength;
    @ApiModelProperty(value = "叶柄细度")
    private String petioleFineness;
    @ApiModelProperty(value = "盛花期")
    private String floweringPeriod;
    @ApiModelProperty(value = "盛熟期")
    private String maturePeriod;
    @ApiModelProperty(value = "座果率(%)")
    private String fruitSettingRate;
    @ApiModelProperty(value = "单芽座果数")
    private String numberFruit;
    @ApiModelProperty(value = "果形")
    private String fruitShape;
    @ApiModelProperty(value = "果色")
    private String fruitColor;
    @ApiModelProperty(value = "果长径(cm)\t")
    private String fruitLength;
    @ApiModelProperty(value = "横径")
    private String transverseDiameter;
    @ApiModelProperty(value = "单果重(g)")
    private String fruitWeight;
    @ApiModelProperty(value = "糖度")
    private String sugarDegree;
    @ApiModelProperty(value = "亩产果量")
    private String yield;
    @ApiModelProperty(value = "适宜区域")
    private String suitableArea;
    @ApiModelProperty(value = "叶面")
    private String leafSurfacePic;
    @ApiModelProperty(value = "叶背")
    private String backLeafPic;
    @ApiModelProperty(value = "枝条")
    private String branchPic;
    @ApiModelProperty(value = "冬芽图")
    private String budsPic;
    @ApiModelProperty(value = "株型")
    private String plantTypePic;
    @ApiModelProperty(value = "果")
    private String fruitPic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(String multiplier) {
        this.multiplier = multiplier;
    }

    public String getBentonite() {
        return bentonite;
    }

    public void setBentonite(String bentonite) {
        this.bentonite = bentonite;
    }

    public String getLeaf_length() {
        return leaf_length;
    }

    public void setLeaf_length(String leaf_length) {
        this.leaf_length = leaf_length;
    }

    public String getLeaf_width() {
        return leaf_width;
    }

    public void setLeaf_width(String leaf_width) {
        this.leaf_width = leaf_width;
    }

    public String getLeaf_weight() {
        return leaf_weight;
    }

    public void setLeaf_weight(String leaf_weight) {
        this.leaf_weight = leaf_weight;
    }

    public String getBreeding_time() {
        return breeding_time;
    }

    public void setBreeding_time(String breeding_time) {
        this.breeding_time = breeding_time;
    }

    public String getTreeType() {
        return treeType;
    }

    public void setTreeType(String treeType) {
        this.treeType = treeType;
    }

    public String getTreeState() {
        return treeState;
    }

    public void setTreeState(String treeState) {
        this.treeState = treeState;
    }

    public String getBranchLength() {
        return branchLength;
    }

    public void setBranchLength(String branchLength) {
        this.branchLength = branchLength;
    }

    public String getBranchThick() {
        return branchThick;
    }

    public void setBranchThick(String branchThick) {
        this.branchThick = branchThick;
    }

    public String getBranchColor() {
        return branchColor;
    }

    public void setBranchColor(String branchColor) {
        this.branchColor = branchColor;
    }

    public String getBranchSpacing() {
        return branchSpacing;
    }

    public void setBranchSpacing(String branchSpacing) {
        this.branchSpacing = branchSpacing;
    }

    public String getCurvature() {
        return curvature;
    }

    public void setCurvature(String curvature) {
        this.curvature = curvature;
    }

    public String getPhyllotaxy() {
        return phyllotaxy;
    }

    public void setPhyllotaxy(String phyllotaxy) {
        this.phyllotaxy = phyllotaxy;
    }

    public String getLeatherColoring() {
        return leatherColoring;
    }

    public void setLeatherColoring(String leatherColoring) {
        this.leatherColoring = leatherColoring;
    }

    public String getPoreDensity() {
        return poreDensity;
    }

    public void setPoreDensity(String poreDensity) {
        this.poreDensity = poreDensity;
    }

    public String getHoleSize() {
        return holeSize;
    }

    public void setHoleSize(String holeSize) {
        this.holeSize = holeSize;
    }

    public String getBudColor() {
        return budColor;
    }

    public void setBudColor(String budColor) {
        this.budColor = budColor;
    }

    public String getBudShape() {
        return budShape;
    }

    public void setBudShape(String budShape) {
        this.budShape = budShape;
    }

    public String getBuds() {
        return buds;
    }

    public void setBuds(String buds) {
        this.buds = buds;
    }

    public String getLeafShape() {
        return leafShape;
    }

    public void setLeafShape(String leafShape) {
        this.leafShape = leafShape;
    }

    public String getSplitLeafShape() {
        return splitLeafShape;
    }

    public void setSplitLeafShape(String splitLeafShape) {
        this.splitLeafShape = splitLeafShape;
    }

    public String getLeafColor() {
        return leafColor;
    }

    public void setLeafColor(String leafColor) {
        this.leafColor = leafColor;
    }

    public String getLeafSpreadingState() {
        return leafSpreadingState;
    }

    public void setLeafSpreadingState(String leafSpreadingState) {
        this.leafSpreadingState = leafSpreadingState;
    }

    public String getLeafTip() {
        return leafTip;
    }

    public void setLeafTip(String leafTip) {
        this.leafTip = leafTip;
    }

    public String getLeafTooth() {
        return leafTooth;
    }

    public void setLeafTooth(String leafTooth) {
        this.leafTooth = leafTooth;
    }

    public String getBrownSmooth() {
        return brownSmooth;
    }

    public void setBrownSmooth(String brownSmooth) {
        this.brownSmooth = brownSmooth;
    }

    public String getLeafCrinkle() {
        return leafCrinkle;
    }

    public void setLeafCrinkle(String leafCrinkle) {
        this.leafCrinkle = leafCrinkle;
    }

    public String getFoliageLuster() {
        return foliageLuster;
    }

    public void setFoliageLuster(String foliageLuster) {
        this.foliageLuster = foliageLuster;
    }

    public String getLeafEcology() {
        return leafEcology;
    }

    public void setLeafEcology(String leafEcology) {
        this.leafEcology = leafEcology;
    }

    public String getPetioleLength() {
        return petioleLength;
    }

    public void setPetioleLength(String petioleLength) {
        this.petioleLength = petioleLength;
    }

    public String getPetioleFineness() {
        return petioleFineness;
    }

    public void setPetioleFineness(String petioleFineness) {
        this.petioleFineness = petioleFineness;
    }

    public String getFloweringPeriod() {
        return floweringPeriod;
    }

    public void setFloweringPeriod(String floweringPeriod) {
        this.floweringPeriod = floweringPeriod;
    }

    public String getMaturePeriod() {
        return maturePeriod;
    }

    public void setMaturePeriod(String maturePeriod) {
        this.maturePeriod = maturePeriod;
    }

    public String getFruitSettingRate() {
        return fruitSettingRate;
    }

    public void setFruitSettingRate(String fruitSettingRate) {
        this.fruitSettingRate = fruitSettingRate;
    }

    public String getNumberFruit() {
        return numberFruit;
    }

    public void setNumberFruit(String numberFruit) {
        this.numberFruit = numberFruit;
    }

    public String getFruitShape() {
        return fruitShape;
    }

    public void setFruitShape(String fruitShape) {
        this.fruitShape = fruitShape;
    }

    public String getFruitColor() {
        return fruitColor;
    }

    public void setFruitColor(String fruitColor) {
        this.fruitColor = fruitColor;
    }

    public String getFruitLength() {
        return fruitLength;
    }

    public void setFruitLength(String fruitLength) {
        this.fruitLength = fruitLength;
    }

    public String getTransverseDiameter() {
        return transverseDiameter;
    }

    public void setTransverseDiameter(String transverseDiameter) {
        this.transverseDiameter = transverseDiameter;
    }

    public String getFruitWeight() {
        return fruitWeight;
    }

    public void setFruitWeight(String fruitWeight) {
        this.fruitWeight = fruitWeight;
    }

    public String getSugarDegree() {
        return sugarDegree;
    }

    public void setSugarDegree(String sugarDegree) {
        this.sugarDegree = sugarDegree;
    }

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }

    public String getSuitableArea() {
        return suitableArea;
    }

    public void setSuitableArea(String suitableArea) {
        this.suitableArea = suitableArea;
    }

    public String getLeafSurfacePic() {
        return leafSurfacePic;
    }

    public void setLeafSurfacePic(String leafSurfacePic) {
        this.leafSurfacePic = leafSurfacePic;
    }

    public String getBackLeafPic() {
        return backLeafPic;
    }

    public void setBackLeafPic(String backLeafPic) {
        this.backLeafPic = backLeafPic;
    }

    public String getBranchPic() {
        return branchPic;
    }

    public void setBranchPic(String branchPic) {
        this.branchPic = branchPic;
    }

    public String getBudsPic() {
        return budsPic;
    }

    public void setBudsPic(String budsPic) {
        this.budsPic = budsPic;
    }

    public String getPlantTypePic() {
        return plantTypePic;
    }

    public void setPlantTypePic(String plantTypePic) {
        this.plantTypePic = plantTypePic;
    }

    public String getFruitPic() {
        return fruitPic;
    }

    public void setFruitPic(String fruitPic) {
        this.fruitPic = fruitPic;
    }

    @Override
    public String toString() {
        return "VarietiesListAdd{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", multiplier='" + multiplier + '\'' +
                ", bentonite='" + bentonite + '\'' +
                ", leaf_length='" + leaf_length + '\'' +
                ", leaf_width='" + leaf_width + '\'' +
                ", leaf_weight='" + leaf_weight + '\'' +
                ", breeding_time='" + breeding_time + '\'' +
                ", treeType='" + treeType + '\'' +
                ", treeState='" + treeState + '\'' +
                ", branchLength='" + branchLength + '\'' +
                ", branchThick='" + branchThick + '\'' +
                ", branchColor='" + branchColor + '\'' +
                ", branchSpacing='" + branchSpacing + '\'' +
                ", curvature='" + curvature + '\'' +
                ", phyllotaxy='" + phyllotaxy + '\'' +
                ", leatherColoring='" + leatherColoring + '\'' +
                ", poreDensity='" + poreDensity + '\'' +
                ", holeSize='" + holeSize + '\'' +
                ", budColor='" + budColor + '\'' +
                ", budShape='" + budShape + '\'' +
                ", buds='" + buds + '\'' +
                ", leafShape='" + leafShape + '\'' +
                ", splitLeafShape='" + splitLeafShape + '\'' +
                ", leafColor='" + leafColor + '\'' +
                ", leafSpreadingState='" + leafSpreadingState + '\'' +
                ", leafTip='" + leafTip + '\'' +
                ", leafTooth='" + leafTooth + '\'' +
                ", brownSmooth='" + brownSmooth + '\'' +
                ", leafCrinkle='" + leafCrinkle + '\'' +
                ", foliageLuster='" + foliageLuster + '\'' +
                ", leafEcology='" + leafEcology + '\'' +
                ", petioleLength='" + petioleLength + '\'' +
                ", petioleFineness='" + petioleFineness + '\'' +
                ", floweringPeriod='" + floweringPeriod + '\'' +
                ", maturePeriod='" + maturePeriod + '\'' +
                ", fruitSettingRate='" + fruitSettingRate + '\'' +
                ", numberFruit='" + numberFruit + '\'' +
                ", fruitShape='" + fruitShape + '\'' +
                ", fruitColor='" + fruitColor + '\'' +
                ", fruitLength='" + fruitLength + '\'' +
                ", transverseDiameter='" + transverseDiameter + '\'' +
                ", fruitWeight='" + fruitWeight + '\'' +
                ", sugarDegree='" + sugarDegree + '\'' +
                ", yield='" + yield + '\'' +
                ", suitableArea='" + suitableArea + '\'' +
                ", leafSurfacePic='" + leafSurfacePic + '\'' +
                ", backLeafPic='" + backLeafPic + '\'' +
                ", branchPic='" + branchPic + '\'' +
                ", budsPic='" + budsPic + '\'' +
                ", plantTypePic='" + plantTypePic + '\'' +
                ", fruitPic='" + fruitPic + '\'' +
                '}';
    }
}
