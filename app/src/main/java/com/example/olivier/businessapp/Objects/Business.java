package com.example.olivier.businessapp.Objects;

public class Business {
    private String name,social,description,pic,pic2,pic3,pic4,adress,city,type,owner,id;
   private int  number,ratings,votes;


    public Business(){

    }
    public Business(String business,String owner,String desc, int num, String pic,String pic2,
                    String pic3,String pic4,String adress,
                    String city, String type , String social, int rank, int total, String id){
        this.name = business;
        this.description=desc;
        this.owner=owner;
        this.number=num;
        this.city=city;
        this.adress=adress;
        this.type=type;
        this.social=social;
        this.pic=pic;
        this.pic2=pic2;
        this.pic3=pic3;
        this.pic4=pic4;
        this.ratings=rank;
        this.votes=total;
        this.id=id;

    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getAdress() {
        return adress;
    }

    public String getCity() {
        return city;
    }

    public String getOwner() {
        return owner;
    }

    public String getPic() {
        return pic;
    }

    public String getPic2() {
        return pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public int getRatings(){ return ratings;}

    public int getVotes(){return votes;}

    public String getSocial() {
        return social;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setBname(String bname) {
        this.name = bname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPic2(String pic) {
        this.pic2 = pic;
    }

    public void setPic3(String pic) {
        this.pic3 = pic;
    }

    public void setPic4(String pic) {
        this.pic4 = pic;
    }
    public void setRatings(int rank) {
        this.ratings = rank;
    }

    public void setVotes(int total) {
        this.votes = total;
    }

    public void setId(String id) {
        this.id = id;
    }





}
