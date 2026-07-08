package com.library.management;

import java.util.Date;

public class Bill {
    // 1. DEĞİŞKENLER (Fields)
    private String billNo;
    private String memberId;
    private double amount;
    private Date date;
    private boolean isPaid; // Faturanın ödenme durumunu takip etmek için

    // 2. CONSTRUCTOR (Yapıcı Metot)
    // Kütüphaneci fatura kestiği an tutar ve üye bilgisi girilir, tarih otomatik 'şu an' olarak atanır.
    public Bill(String billNo, String memberId, double amount) {
        this.billNo = billNo;
        this.memberId = memberId;
        this.amount = amount;
        this.date = new Date(); // Faturanın kesildiği anki sistem tarihi
        this.isPaid = false;    // Yeni kesilen fatura varsayılan olarak ödenmemiştir
    }

    // 3. ANA İŞ METOTLARI (Business Methods)

    // Üye cezayı ödemeye geldiğinde bu metot çağrılır
    public void billPay() {
        if (isPaid) {
            System.out.println("[FATURA] : " + billNo + " numaralı fatura zaten daha önce ödenmiş.");
        } else {
            this.isPaid = true;
            System.out.println("[FATURA] : " + billNo + " numaralı, " + amount + " TL tutarındaki fatura başarıyla ödendi.");
        }
    }

    // Fatura detaylarını makbuz gibi ekrana basmak için şık bir metot
    public void displayBill() {
        System.out.println("\n------- FATURA MAKBUZU -------");
        System.out.println("Fatura No   : " + billNo);
        System.out.println("Üye ID      : " + memberId);
        System.out.println("Tutar       : " + amount + " TL");
        System.out.println("Tarih       : " + date);
        System.out.println("Durum       : " + (isPaid ? "ÖDENDİ" : "ÖDENMEDİ"));
        System.out.println("------------------------------\n");
    }

    // 4. GETTER & SETTER METOTLARI (En Altta)
    public String getBillNo() {
        return billNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public boolean isPaid() {
        return isPaid;
    }
}