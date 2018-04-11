import java.io.File
import java.io.InputStream

fun fileReader(): ArrayList<Scene>{
    val listaCenas = ArrayList<Scene>()

    val stream: InputStream = File("/Users/cogeti/Documents/Wacky-Race/src/Cenas.txt").inputStream()
    val str = stream.bufferedReader().use { it.readText() }
    val reg = Regex(";;")
    var list = str.split(reg)

    for (i in list){
        val regAux = Regex(";")
        val listAux = i.split(regAux)

        val listaObjetos = ArrayList<Objects>()

        var j = 3

        while (j < (listAux.size)-1){
            var objAux = Objects(Integer.parseInt(listAux[j++]),
                                 listAux[j++],
                                 Integer.parseInt(listAux[j++]),
                                 listAux[j++],
                                 listAux[j++],
                                 Integer.parseInt(listAux[j++]))
            listaObjetos.add(objAux)
        }

        val scene = Scene(Integer.parseInt(listAux[0]), listAux[1], listAux[2], listaObjetos)
        listaCenas.add(scene)
    }

    return listaCenas
}

fun newGame(listaCenas: ArrayList<Scene>): Game{
    return Game(listaCenas, 0)
}

var inventory = Inventory()
fun runGame(jogo: Game): Int{
    do{
        println("Cena Atual: "+jogo.scenes[jogo.cena_atual].titulo)
        println("Descrição: "+jogo.scenes[jogo.cena_atual].descricao)
        println("Ação: ")
        var escolha: String = readLine()!!

        val reg = Regex(" ")
        val list = escolha.split(reg)

        var acao = list[0]
        var objeto = list[1]

        var objetoEncontrado = Objects()
        var objetoOk = false

        for(i in jogo.scenes[jogo.cena_atual].itens){
            if(i.nome.equals(objeto, true) && i.comando_correto.equals(acao, true)) {
                objetoEncontrado = i
                objetoOk = true
            }
        }

        if(objetoOk){
            if(objetoEncontrado.comando_correto.equals("use", true)){
                jogo.cena_atual = objetoEncontrado.cena_alvo
            }
            else{
                objetoEncontrado.comando_correto = "USE"
                inventory.itens.add(objetoEncontrado)
            }
        }
        else{
            println("Objeto ou Ação incorretos!")
        }
    }
    while (jogo.cena_atual != 16)

    return 2
}

fun printMenu(){
    println("----Welcome To Wacky-Race:----\n" +
            "|                           |\n" +
            "| 1: New Game               |\n" +
            "| 2: End Game               |\n" +
            "| 3: Listar Inventário      |\n" +
            "| 4: Help                   |\n" +
            "-----------------------------")
}

fun menuOption(){
    printMenu()

    println("Escolha uma opção:")
    var escolha:Int = readLine()!!.toInt()

    when(escolha){
        1 ->{
            println("Escolhi Novo Jogo!")
            escolha = runGame(newGame(fileReader()))
        }
        2 -> {
            println("Fim de Jogo!")
            return
        }
        3 -> {
            printInventario(inventory)
        }
    }
}

fun printInventario(listaInventario: Inventory){
    println("Lista de objetos no Inventário:")
    for (i in listaInventario.itens){
        println(i.toString())
    }
}

fun main(args: Array<String>) {
    menuOption()
}