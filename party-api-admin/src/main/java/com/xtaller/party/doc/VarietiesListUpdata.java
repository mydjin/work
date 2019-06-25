package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "品总列表新增")
public class VarietiesListUpdata {

    @ApiModelProperty(value = "Id")
    private String id;
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
    private Integer treeState = 0;
    @ApiModelProperty(value = "枝长")
    private Integer branchLength = 0;
    @ApiModelProperty(value = "枝粗度")
    private Integer branchThick = 0;
    @ApiModelProperty(value = "皮色")
    private Integer branchColor = 0;
    @ApiModelProperty(value = "节距(cm)")
    private String branchSpacing;
    @ApiModelProperty(value = "节曲直")
    private Integer curvature = 0;
    @ApiModelProperty(value = "叶序")
    private Integer phyllotaxy = 0;
    @ApiModelProperty(value = "皮孔色")
    private Integer leatherColoring = 0;
    @ApiModelProperty(value = "皮孔密度")
    private Integer poreDensity = 0;
    @ApiModelProperty(value = "皮孔大小")
    private Integer holeSize = 0;
    @ApiModelProperty(value = "芽色")
    private Integer budColor = 0;
    @ApiModelProperty(value = "芽生状")
    private Integer budShape = 0;
    @ApiModelProperty(value = "副芽")
    private Integer buds = 0;
    @ApiModelProperty(value = "全叶形")
    private Integer leafShape = 0;
    @ApiModelProperty(value = "裂叶形")
    private Integer splitLeafShape = 0;
    @ApiModelProperty(value = "叶色")
    private Integer leafColor = 0;
    @ApiModelProperty(value = "叶展态")
    private Integer leafSpreadingState = 0;
    @ApiModelProperty(value = "叶尖")
    private Integer leafTip = 0;
    @ApiModelProperty(value = "叶缘齿")
    private Integer leafTooth = 0;
    @ApiModelProperty(value = "叶糙滑")
    private Integer brownSmooth = 0;
    @ApiModelProperty(value = "叶缩皱")
    private Integer leafCrinkle = 0;
    @ApiModelProperty(value = "叶面光泽")
    private Integer foliageLuster = 0;
    @ApiModelProperty(value = "叶着生态")
    private Integer leafEcology = 0;
    @ApiModelProperty(value = "叶柄长度")
    private Integer petioleLength = 0;
    @ApiModelProperty(value = "叶柄细度")
    private Integer petioleFineness = 0;
    @ApiModelProperty(value = "盛花期")
    private String floweringPeriod;
    @ApiModelProperty(value = "盛熟期")
    private String maturePeriod;
    @ApiModelProperty(value = "座果率(%)")
    private String fruitSettingRate;
    @ApiModelProperty(value = "单芽座果数")
    private String numberFruit;
    @ApiModelProperty(value = "果形")
    private Integer fruitShape = 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getTreeState() {
        return treeState;
    }

    public void setTreeState(Integer treeState) {
        this.treeState = treeState;
    }

    public Integer getBranchLength() {
        return branchLength;
    }

    public void setBranchLength(Integer branchLength) {
        this.branchLength = branchLength;
    }

    public Integer getBranchThick() {
        return branchThick;
    }

    public void setBranchThick(Integer branchThick) {
        this.branchThick = branchThick;
    }

    public Integer getBranchColor() {
        return branchColor;
    }

    public void setBranchColor(Integer branchColor) {
        this.branchColor = branchColor;
    }

    public String getBranchSpacing() {
        return branchSpacing;
    }

    public void setBranchSpacing(String branchSpacing) {
        this.branchSpacing = branchSpacing;
    }

    public Integer getCurvature() {
        return curvature;
    }

    public void setCurvature(Integer curvature) {
        this.curvature = curvature;
    }

    public Integer getPhyllotaxy() {
        return phyllotaxy;
    }

    public void setPhyllotaxy(Integer phyllotaxy) {
        this.phyllotaxy = phyllotaxy;
    }

    public Integer getLeatherColoring() {
        return leatherColoring;
    }

    public void setLeatherColoring(Integer leatherColoring) {
        this.leatherColoring = leatherColoring;
    }

    public Integer getPoreDensity() {
        return poreDensity;
    }

    public void setPoreDensity(Integer poreDensity) {
        this.poreDensity = poreDensity;
    }

    public Integer getHoleSize() {
        return holeSize;
    }

    public void setHoleSize(Integer holeSize) {
        this.holeSize = holeSize;
    }

    public Integer getBudColor() {
        return budColor;
    }

    public void setBudColor(Integer budColor) {
        this.budColor = budColor;
    }

    public Integer getBudShape() {
        return budShape;
    }

    public void setBudShape(Integer budShape) {
        this.budShape = budShape;
    }

    public Integer getBuds() {
        return buds;
    }

    public void setBuds(Integer buds) {
        this.buds = buds;
    }

    public Integer getLeafShape() {
        return leafShape;
    }

    public void setLeafShape(Integer leafShape) {
        this.leafShape = leafShape;
    }

    public Integer getSplitLeafShape() {
        return splitLeafShape;
    }

    public void setSplitLeafShape(Integer splitLeafShape) {
        this.splitLeafShape = splitLeafShape;
    }

    public Integer getLeafColor() {
        return leafColor;
    }

    public void setLeafColor(Integer leafColor) {
        this.leafColor = leafColor;
    }

    public Integer getLeafSpreadingState() {
        return leafSpreadingState;
    }

    public void setLeafSpreadingState(Integer leafSpreadingState) {
        this.leafSpreadingState = leafSpreadingState;
    }

    public Integer getLeafTip() {
        return leafTip;
    }

    public void setLeafTip(Integer leafTip) {
        this.leafTip = leafTip;
    }

    public Integer getLeafTooth() {
        return leafTooth;
    }

    public void setLeafTooth(Integer leafTooth) {
        this.leafTooth = leafTooth;
    }

    public Integer getBrownSmooth() {
        return brownSmooth;
    }

    public void setBrownSmooth(Integer brownSmooth) {
        this.brownSmooth = brownSmooth;
    }

    public Integer getLeafCrinkle() {
        return leafCrinkle;
    }

    public void setLeafCrinkle(Integer leafCrinkle) {
        this.leafCrinkle = leafCrinkle;
    }

    public Integer getFoliageLuster() {
        return foliageLuster;
    }

    public void setFoliageLuster(Integer foliageLuster) {
        this.foliageLuster = foliageLuster;
    }

    public Integer getLeafEcology() {
        return leafEcology;
    }

    public void setLeafEcology(Integer leafEcology) {
        this.leafEcology = leafEcology;
    }

    public Integer getPetioleLength() {
        return petioleLength;
    }

    public void setPetioleLength(Integer petioleLength) {
        this.petioleLength = petioleLength;
    }

    public Integer getPetioleFineness() {
        return petioleFineness;
    }

    public void setPetioleFineness(Integer petioleFineness) {
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

    public Integer getFruitShape() {
        return fruitShape;
    }

    public void setFruitShape(Integer fruitShape) {
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
        return "VarietiesListUpdata{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", multiplier='" + multiplier + '\'' +
                ", bentonite='" + bentonite + '\'' +
                ", leaf_length='" + leaf_length + '\'' +
                ", leaf_width='" + leaf_width + '\'' +
                ", leaf_weight='" + leaf_weight + '\'' +
                ", breeding_time='" + breeding_time + '\'' +
                ", treeType='" + treeType + '\'' +
                ", treeState=" + treeState +
                ", branchLength=" + branchLength +
                ", branchThick=" + branchThick +
                ", branchColor=" + branchColor +
                ", branchSpacing='" + branchSpacing + '\'' +
                ", curvature=" + curvature +
                ", phyllotaxy=" + phyllotaxy +
                ", leatherColoring=" + leatherColoring +
                ", poreDensity=" + poreDensity +
                ", holeSize=" + holeSize +
                ", budColor=" + budColor +
                ", budShape=" + budShape +
                ", buds=" + buds +
                ", leafShape=" + leafShape +
                ", splitLeafShape=" + splitLeafShape +
                ", leafColor=" + leafColor +
                ", leafSpreadingState=" + leafSpreadingState +
                ", leafTip=" + leafTip +
                ", leafTooth=" + leafTooth +
                ", brownSmooth=" + brownSmooth +
                ", leafCrinkle=" + leafCrinkle +
                ", foliageLuster=" + foliageLuster +
                ", leafEcology=" + leafEcology +
                ", petioleLength=" + petioleLength +
                ", petioleFineness=" + petioleFineness +
                ", floweringPeriod='" + floweringPeriod + '\'' +
                ", maturePeriod='" + maturePeriod + '\'' +
                ", fruitSettingRate='" + fruitSettingRate + '\'' +
                ", numberFruit='" + numberFruit + '\'' +
                ", fruitShape=" + fruitShape +
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
