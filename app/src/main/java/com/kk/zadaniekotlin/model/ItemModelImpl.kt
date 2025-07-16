package com.kk.zadaniekotlin.model

class ItemModelImpl : ItemModel {

    override fun getItems(): List<Item> = listOf(
        Item("Kurtka damska MERIDA beżowa Dstreet", "189,99 zł", "https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcRSGw93R_ibXEJPnyxs5REGS7AFUyj6LcnTQ9I8FbYi9AAPwXvQcgSj6u9NHskKaH5kbflHd4CfHYQpGBpbfjpBgwndHnBayDOSLerMtLyaPCo7BjtpuFWcPtn_CfFq5l43IbR1QtlW&usqp=CAc"),
        Item("Kurtka męska skórzana czarna Dstreet", "159,99 zł", "https://dstreet.pl/hpeciai/0ae1a66b654a11b1363b84c79959867d/pol_pl_Kurtka-meska-skorzana-czarna-Dstreet-TX4387-45489_3.webp"),
        Item("Kurtka męska granatowa Dstreet", "159,99 zł", "https://dstreet.pl/hpeciai/e184093a9a6f8ad53de2d32488059439/pol_pm_Kurtka-meska-przejsciowa-granatowa-Dstreet-TX5006-54291_3.jpg"),
        Item("Kurtka męska skórzana czarna Dstreet", "89,99 zł", "https://lb5.dstatic.pl/images/product-thumb/176335281-kurtka-meska-bomber-jacket-czarna-dstreet-tx4423-dstreet-m-dstreet-pl.jpg")
    )
}