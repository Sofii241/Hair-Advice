package com.example.escaner.Model.Productos

import com.example.escaner.Model.Usuarios.Enums.Nivel
import com.example.escaner.R

class ProductoManager {

    companion object {
        private var productos = mutableListOf<Producto>()

        fun cargarProductos() {
            var producto = Producto(
                "8480000465689",
                "Champú Repair & Nutricion",
                "Deliplus",
                arrayListOf(
                    "Agua",
                    "Alcohol Cetearílico",
                    "Amodimeticona",
                    "Cloruro de behentrimonio",
                    "Octildodecanol",
                    "Ácido Hialuronico",
                    "Queratina Hidrolizada",
                    "Arginina"
                ),
                arrayListOf("Cabello seco y dañado"),
                arrayListOf(
                    Recomendacion("Seco", Nivel.Recomendado, "Recomendado en cabellos secos para recuperar la hidratación y reparar el cabello áspero por daños en la estructura interna."),
                    Recomendacion("Graso", Nivel.NoRecomendado, "No recomendado para cabello graso, puede aumentar la grasa en el cuero cabelludo debido a sus compuestos que buscan incrementar la hidratación."),
                    Recomendacion("Natural", Nivel.Recomendado, "Excelente para nutrir y fortalecer el cabello sin tratamientos químicos."),
                    Recomendacion("Tenido", Nivel.Recomendado, "Recomendado para cabellos teñidos, ayuda a mantener el cabello hidratado y saludable sin afectar al color."),
                    Recomendacion("Afro", Nivel.Recomendado, "Ayuda a hidratar y definir la textura natural del cabello afro."),
                    Recomendacion("Liso", Nivel.Recomendado, "Ideal para mantener el acabado en cabellos lisos, hidratarlo y que sea manejable."),
                    Recomendacion("Ondulado", Nivel.Recomendado, "Proporciona hidratación y ayuda a definir las ondas naturales suavemente."),
                    Recomendacion("Hidratación", Nivel.Recomendado, "Excelente para reforzar los tratamientos de hidratación"),
                    Recomendacion("Alisado", Nivel.Recomendado, "Apto para mantener la suavidad después de un tratamiento de alisado sin afectar al acabado"),
                    Recomendacion("Ondas", Nivel.Recomendado, "Apto para hidratar el cabello sin afectar el acabado de un tratamiento de ondas permanentes.")
                ),
                arrayListOf("Hidrata el cabello gracias a que esta formado por ácido hialurónico y lo fortalece con queratina hidrolizada" +
                        "La arginina ayuda a mejorar la circulación del cuero cabelludo, promoviendo un cabello más sano y fuerte"),
                R.drawable.id8440000465689
            )
            registrarProducto(producto)

            producto = Producto(
                "8480000443373",
                "Champú Argan oil",
                "Deliplus",
                arrayListOf(
                    "Agua",
                    "Sulfato de sodio laureth",
                    "Lauroandoacetato de Sodio",
                    "Dimeticona",
                    "Cloruro de Sodio",
                    "Amodimeticona",
                    "Copolímero de Acrilatos",
                    "Cocamide"
                ),
                arrayListOf("Nutrición e hidratación"),
                arrayListOf(
                    Recomendacion("Seco", Nivel.Recomendado, "Nutre y repara el cabello seco desde su estructura interna."),
                    Recomendacion("Graso", Nivel.NoRecomendado, "No recomendado en cabello graso por su contenido en aceites que pueden aumentar el cebo."),
                    Recomendacion("Mixto", Nivel.NoRecomendado, "No recomendado para cabellos mixtos si el cabello  tiende a engrasarse fácilmente."),
                    Recomendacion("Natural", Nivel.Recomendado, "Excelente para el mantenimiento de cabello natural, ayudando a preservar su salud."),
                    Recomendacion("Alisado", Nivel.Recomendado, "Complemento perfecto para un tratamiento de alisado, ayuda a mantener el cabello liso gracias a sus ingredientes suavizantes como el amodimeticona."),
                    Recomendacion("Tinte", Nivel.Recomendado, "Beneficioso para mantener la hidratación en cabellos tratados quimicamente, aportando suavidad y manejo del cabello sin sobrecargarlo."),
                    Recomendacion("Hidratación", Nivel.Recomendado, "Apto para usar a diario en cabellos con un tratamiento de hidratación, ayudando a incrementar los resultados del tratamiento"),
                    Recomendacion("Ondas", Nivel.NoRecomendado, "No recomendado para uso regular en cabellos con ondas permanentes que requieran mantener la definición, ya que los ingredientes suavizantes pueden aflojar la estructura de las ondas."),
                    Recomendacion("Afro", Nivel.NoRecomendado, "No recomendado para cabellos afro muy gruesos y densos, donde podría no distribuirse de manera uniforme y ser difícil de enjuagar completamente.")
                ),
                arrayListOf("Nutre y suaviza el cabello con aceite de argán y amodimeticona, proporcionando un brillo natural y reduciendo el frizz"),
                R.drawable.id8440000443373
            )
            registrarProducto(producto)

            producto = Producto(
                "3600523277520",
                "Champú Color Vive Elvive",
                "Loreal Paris",
                arrayListOf("Agua",
                    "Sulfato de sodio laureth",
                    "Dimeticona",
                    "Coco-Betaína",
                    "Cloruro de Sodio",
                    "Distearato de Glicol",
                    "Cloruro de Guar Hidroxipropiltriamonio",
                    "MIPA de Cocamida" ),
                arrayListOf("Cabello teñido o con mechas"),
                arrayListOf(
                    Recomendacion("Seco", Nivel.Intermedio, "En cabellos secos ayuda a preservar el color del tinte pero debe combinarse con champús hidratantes."),
                    Recomendacion("Graso", Nivel.Recomendado, "Recomendando para preservar el color del tinte en cabellos grasos sin producir más cebo"),
                    Recomendacion("Mixto", Nivel.Recomendado, "Apto para usarse en cabellos mixtos que necesiten cuidar el color del tinte."),
                    Recomendacion("Natural", Nivel.NoRecomendado, "No recomendado para cabellos sin tratamiento químicos, este producto esta diseñado para mantener el color del tinte."),
                    Recomendacion("Tenido", Nivel.Recomendado, "Producto imprescindible para ayudar a mantener el color del tinte en cabellos teñidos."),
                    Recomendacion("Afro", Nivel.Recomendado, "Aporta protección del color y antioxidantes que benefician cualquier tipo de cabello, incluyendo afro."),
                    Recomendacion("Liso", Nivel.Recomendado, "Ideal para mantener el color del tinte en el cabello liso."),
                    Recomendacion("Ondulado", Nivel.Recomendado, "Apto para pelo ondulado, protegiendo el color sin afectar a las ondas."),
                    Recomendacion("Hidratacion", Nivel.Recomendado, "Adecuado para cabellos con tratamientos de hidratación, sus ingredientes ayudan a mantener el color del tinte y el cabello hidratado y protegido."),
                    Recomendacion("Alisado", Nivel.NoRecomendado, "No recomendable en tratamiento de alisado, contiene sulfatos para el mantenimiento del color de desharán el alisado."),
                    Recomendacion("Ondas", Nivel.Recomendado, "Util para proteger el color del tinte en cabellos con un tratamiento de ondas sin afectar al tratamiento."),
                ),
                arrayListOf("Específicamente formulado para cabellos teñidos o con mechas, protegiendo el color y evitando su desgaste",
                    " Contiene filtros UV y antioxidantes que protegen el color del cabello",
                    "Sus ingredientes limpian suavemente sin despojar al cabello de sus aceites naturales, hidratandolo a la vez que mantiene el color"),
                R.drawable.id3600524153458
            )
            registrarProducto(producto)

            producto = Producto(
                "3600524153465",
                "Champú Hidra Hialurónico",
                "Loreal Paris",
                arrayListOf("Agua",
                    "Sulfato de sodio laureth",
                    "Dimeticona",
                    "Cloruro de Sodio",
                    "Betaína de Cocamidopropil",
                    "Fructosa",
                    "Cloruro de Hidroxiprol"),
                arrayListOf("Cabello seco"),
                arrayListOf(
                    Recomendacion("Natural", Nivel.Recomendado, "Ideal para cabello natural que necesita una hidratación."),
                    Recomendacion("Tenido", Nivel.Recomendado, "Altamente recomendado para cabellos teñidos que requieren máxima hidratación para reparar y proteger."),
                    Recomendacion("Graso", Nivel.NoRecomendado, "Uso no recomendado en cabellos grasos, es un producto muy hidratante que puede provocar más grasa en el cabello."),
                    Recomendacion("Seco", Nivel.Recomendado, "Perfecto para revitalizar y añadir una hidratación duradera a cabellos secos."),
                    Recomendacion("Mixto", Nivel.NoRecomendado, "Evitar su uso diario  en cabellos mixtos si tiende a ser muy graso en las raíces."),
                    Recomendacion("Afro", Nivel.Recomendado, "Excelente para proporcionar la hidratación necesaria sin apelmazar, manteniendo rizos definidos y saludables."),
                    Recomendacion("Liso", Nivel.Recomendado, "Ayuda a mantener el cabello liso suave y manejable."),
                    Recomendacion("Ondulado", Nivel.Recomendado, "Hidrata las ondas sin perder la definición natural. Reconstruye las ondas internamente sin afectar al acabado del pelo."),
                    Recomendacion("Alisado", Nivel.Recomendado, "Apto para usarse en cabellos con un tratamiento de alisado, manteniendolo alisado, hidratado y protegido."),
                    Recomendacion("Ondas", Nivel.Recomendado, "Apto para cabellos con un tratamiento de ondas, aporta la hidratación necesaria para mantener las ondas saludables y definidas sin afectar al acabado."),
                ),
                arrayListOf(" La fórmula enriquecida con hialuronato de sodio atrae y retiene la humedad en el cabello generando una hidratación profunda",
                    "Aporta suavidad y controla el encrespamiento"),
                R.drawable.id3600524153465
            )
            registrarProducto(producto)

            producto = Producto(
                "8480000226297",
                "Champú Curl Perfect",
                "Deliplus",
                arrayListOf("Agua",
                    "Laureth Sulfosuccinato",
                    "Betaína de Cocamidopropil",
                    "Perfume",
                    "Cloruro de Sodio",
                    "Cocoato de Glicerilo",
                    "Benzoato de Sodio",
                    "Fenoxietanol"),
                arrayListOf("Cabello ondulado, rizado o afro"),
                arrayListOf(
                    Recomendacion("Natural", Nivel.Recomendado, "Apto para cabello ondulado sin tratamientos quimicos, realza la textura natural y mejora la definición."),
                    Recomendacion("Liso", Nivel.NoRecomendado, "No recomendado para cabellos lisos, el producto esta diseñado para cabellos ondulados, rizados o afro."),
                    Recomendacion("Tenido", Nivel.Recomendado, "Uso seguro para cabello teñido, ayuda a definir rizos sin desvanecer el color."),
                    Recomendacion("Graso", Nivel.NoRecomendado, "No recomendado para cabellos grasos, puede incentivar el cebo dando efecto de suciedad."),
                    Recomendacion("Seco", Nivel.Recomendado, "Hidrata y define rizos, ideal para cabello seco que necesita nutrición."),
                    Recomendacion("Mixto", Nivel.NoRecomendado, "No recomendado apra cabellos mixtos que tienden a ser más grasos en las raices, acentuará el efecto y lo apelmazará."),
                    Recomendacion("Afro", Nivel.Recomendado, "Ayuda a mantener la definición natural y manejo de rizos afro."),
                    Recomendacion("Ondulado", Nivel.Recomendado, "Ideal para potenciar la definición natural y controlar el frizz en cabellos ondulados."),
                    Recomendacion("Alisado", Nivel.NoRecomendado, "No recomendado para cabellos con tratamientos de alisado, deshará los efectos del tratamiento."),
                    Recomendacion("Hidratación", Nivel.Recomendado, "Buen complemento con tratamientos de hidratación para rizos o cabellos ondulados."),
                    Recomendacion("Ondas", Nivel.Recomendado, "Excelente para mantener y definir tratamientos de ondas permanentes, dar volumen al pelo y mantenerlo sano.")
                ),
                arrayListOf("Ayuda a definir y mantener los rizos y controla el frizz con ingredientes como el betaína de cocamidopropil y el benzoato de sodio, que también protegen el cabello de los daños ambientales"),
                R.drawable.id8480000226297

            )
            registrarProducto(producto)


            producto = Producto(
                "8480000443632",
                "Mascarilla Repair & Nutricion",
                "Deliplus",
                arrayListOf(
                    "Agua",
                    "Alcohol Cetearílico",
                    "Amodimeticona",
                    "Cloruro de Behentrimonio",
                    "Extracto de Caviar",
                    "Queratina Hidrolizada",
                    "Vitamina E",
                    "Arginina"
                ),
                arrayListOf("Cabello seco y dañado"),
            arrayListOf(
                Recomendacion("Natural", Nivel.Recomendado, "Ideal para fortalecer y nutrir el cabello natural, promoviendo un cabello más saludable."),
                Recomendacion("Tenido", Nivel.Recomendado, "Ayuda a reparar el daño del tinte en el cabello, restaurando su brillo y suavidad."),
                Recomendacion("Graso", Nivel.Intermedio, "En cabellos grasos aplicar solamente en las puntas para evitar agregar grasa al cuero cabelludo."),
                Recomendacion("Seco", Nivel.Recomendado, "Proporciona intensa hidratación y reparación para cabellos secos."),
                Recomendacion("Mixto", Nivel.Recomendado, "En cabellos mixtos aplicar en áreas secas para mejorar la textura y la hidratación sin afectar las zonas grasas."),
                Recomendacion("Afro", Nivel.Recomendado, "Ideal para aportar hidratación y manejo a cabellos afro, ayudando a definir rizos sin apelmazar."),
                Recomendacion("Liso", Nivel.Recomendado, "Recomendado para cabellos lisos, nutre el cabello, facilita el manejo y reduce el frizz."),
                Recomendacion("Ondulado", Nivel.Recomendado, "Ayuda a definir ondas al aportar hidratación y reducir el frizz, manteniendo la forma natural del pelo."),
                Recomendacion("Alisado", Nivel.Recomendado, "Contribuye a mantener tratamientos alisantes en el cabello, aportando nutrición en la estructura interna."),
                Recomendacion("Hidratacion", Nivel.Recomendado, "Excelente complemento para tratamientos de hidratación intensificando los resultados."),
                Recomendacion("Ondas", Nivel.Recomendado, "Uso seguro en cabellos con tratamientos de ondas, porta nutrición y mantiene la definición sin afectar al acabado."),
                ),
            arrayListOf("Profundiza la nutrición y la reparación con extracto de caviar y vitaminas, dejando el cabello suave, manejable y revitalizado"),
                R.drawable.id8480000443632
            )
            registrarProducto(producto)

            producto = Producto(
                "8480000443380",
                "Mascarilla Argan Oil",
                "Deliplus",
                arrayListOf(
                    "Agua",
                    "Alcohol Cetearílico",
                    "Cloruro de Behentrimonio",
                    "Amodimeticona",
                    "Ésteres de Cetilo",
                    "Hidroxietilcelulosa",
                    "Fenil",
                    "Aceite de Argán"
                ),
                arrayListOf("Cabello seco y a falta de hidratación"),
                arrayListOf(
                    Recomendacion("Natural", Nivel.Recomendado, "Ideal para cabello natural, aporta nutrición y brillo sin químicos pesados."),
                    Recomendacion("Tenido", Nivel.Recomendado, "Nutre y protege el color, manteniendo el cabello vibrante y saludable."),
                    Recomendacion("Graso", Nivel.Intermedio, "En cabellos grasos aplicar solo en las puntas para evitar que las raíces generen más cebo."),
                    Recomendacion("Seco", Nivel.Recomendado, "Proporciona una hidratación intensiva y restaura la suavidad en cabellos secos."),
                    Recomendacion("Mixto", Nivel.Recomendado, "En cabellos mixtos utilizar enfocándose en las áreas secas, evitando las partes grasas como el cuero cabelludo."),
                    Recomendacion("Afro", Nivel.Recomendado, "Aporta la hidratación y los nutrientes necesarios para mantener el cabello afro manejable y sano."),
                    Recomendacion("Liso", Nivel.Recomendado, "Ayuda a suavizar y dar brillo al cabello liso, reduciendo el frizz."),
                    Recomendacion("Ondulado", Nivel.Recomendado, "Nutre y define las ondas, controlando el frizz sin reducir su forma natural."),
                    Recomendacion("Alisado", Nivel.Recomendado, "Ayuda a mantener el cabello con tratamiento de alisado nutrido y suave, sin afectar al acabado."),
                    Recomendacion("Hidratacion", Nivel.Recomendado, "Complementa perfectamente los tratamientos hidratantes, reforzando su efectividad."),
                    Recomendacion("Ondas", Nivel.Recomendado,"Un aliado perfecto para mantener el pelo sano después de un tratamiento de ondas, sin afectar al efecto.")
                ),
                arrayListOf("Nutre intensamente con aceite de argán y ésteres de cetilo, aportando suavidad y brillo sin perder el volumen natural"),
                R.drawable.id8480000443380
            )
            registrarProducto(producto)

            producto = Producto(
                "8411300710001",
                "Mascarilla Protector Color",
                "Loreal Paris",
                arrayListOf(
                    "Agua",
                    "Alcohol Cetearílico",
                    "Cloruro de Behentrimonio",
                    "Ésteres de Cetilo",
                    "Aceite de Coco",
                    "Benzoato de Sodio",
                    "Fenoxietanol",
                    "Extracto de Flor de Peonía"
                ),
                arrayListOf("Cabello teñido"),
                arrayListOf(
                    Recomendacion("Natural", Nivel.NoRecomendado, "Evitar en cabello sin tinte, este producto está diseñado para cabello teñido y puede dañar el cabello."),
                    Recomendacion("Tenido", Nivel.Recomendado, "Excelente para proteger el color y mejorar la suavidad y el brillo del cabello teñido."),
                    Recomendacion("Graso", Nivel.Intermedio, "En cabello graso usar solo en las puntas para evitar generar más cebo."),
                    Recomendacion("Seco", Nivel.Recomendado, "Proporciona una hidratación intensa, ideal para cabellos secos."),
                    Recomendacion("Mixto", Nivel.Recomendado, "En cabellos mixtos aplicar de medios a puntas para hidratar el pelo y mantener el color sin tocar las raices."),
                    Recomendacion("Afro", Nivel.Recomendado, "Nutre y protege el cabello afro teñido, manteniendo la salud y el color vibrante."),
                    Recomendacion("Liso", Nivel.Recomendado, "Ayuda a mantener el color y la suavidad del cabello liso teñido."),
                    Recomendacion("Ondulado", Nivel.Recomendado, "Ideal para proteger el color de cabellos ondulados y mejorar la definición."),
                    Recomendacion("Alisado", Nivel.Recomendado, "Protege el color sin afectar al resultado de tratamientos alisantes."),
                    Recomendacion("Hidratacion", Nivel.Recomendado, "Apto para usarse en cabellos con tratamientos de hidratación que estén teñidos, ayudando a preservar el color."),
                    Recomendacion("Ondas", Nivel.Recomendado, "Apto para usarse en cabellos con tratamientos de ondas permanentes sin afectar a la definición de las ondas.")

                ),
                arrayListOf("Enriquecida con antioxidantes y filtros UV para proteger el color, aceite de coco para nutrir y fortalecer la estructura el cabello teñido"),
                R.drawable.id8411300710001
            )
            registrarProducto(producto)

            producto = Producto(
                "3600524030797",
                "Mascarilla Hidra Hialurónico",
                "Loreal Paris",
                arrayListOf(
                    "Agua",
                    "Glicerina",
                    "Alcohol Cetearílico",
                    "Fosfato de Distarca",
                    "Copolímero SMDI/Alcohol",
                    "Queratina Hidrolizada",
                    "Behentrim",
                    "Perfume"
                ),
                arrayListOf("Cabello seco"),
                arrayListOf(
                    Recomendacion("Natural", Nivel.Recomendado, "Excelente para añadir hidratación y suavidad al cabello sin tratamientos químicos."),
                    Recomendacion("Tenido", Nivel.Recomendado, "Ideal para mantener el cabello teñido hidratado y vibrante, prolongando el color."),
                    Recomendacion("Graso", Nivel.Intermedio, "En cabello graso aplicar solo en las puntas para evitar agregar más cebo al cuero cabelludo."),
                    Recomendacion("Seco", Nivel.Recomendado, "Proporciona una hidratación profunda, perfecta para cabellos secos que necesitan un impulso de humedad."),
                    Recomendacion("Mixto", Nivel.Intermedio, "En cabellos mixtos usar de medios a puntas, evitando las raíces grasas para equilibrar la sobre hidratación."),
                    Recomendacion("Afro", Nivel.Recomendado, "Excelente para hidratar y nutrir el cabello afro, proporcionando manejo y reducción de frizz."),
                    Recomendacion("Liso", Nivel.Recomendado, "Aporta suavidad y brillo, manteniendo el cabello liso saludable tratando su estructura interna."),
                    Recomendacion("Ondulado", Nivel.Recomendado, "Ideal para hidratar y definir ondas sin afectar a la textura natural."),
                    Recomendacion("Alisado", Nivel.Recomendado, "Mantiene el efecto del tratamiento alisante, hidratando y suavizando el cabello."),
                    Recomendacion("Hidratacion", Nivel.Recomendado, "Complementa y potencia tratamientos hidratantes, asegurando una hidratación duradera."),
                    Recomendacion("Ondas", Nivel.Recomendado, "Apto para usarse en cabellos con tratamientos de ondas permanentes sin afectar a la definición de las ondas.")

                ),
                arrayListOf("Maximiza la hidratación y restaura la elasticidad del cabello con glicerina y ácido hialurónico, dejando el cabello suave y revitalizado"),
                R.drawable.id3600524030797
            )
            registrarProducto(producto)



            producto = Producto(
                "8480000860781",
                "Mascarilla Curl Perfect",
                "Deliplus",
                arrayListOf(
                    "Agua",
                    "Alcohol Cetearílico",
                    "Dimetilamina Stearamidopropil",
                    "Cloruro de Cetrimonio",
                    "Ácido Hialurónico",
                    "Fructosa",
                    "Ácido Cítrico",
                    "Betaína"
                ),
                arrayListOf("Cabello ondulado"),
                arrayListOf(
                    Recomendacion("Natural", Nivel.Recomendado, "Uso recomendado para definir rizos naturales y mantener la hidratación sin apelmazar."),
                    Recomendacion("Tenido", Nivel.Recomendado, "Ayuda a mantener la definición y el brillo del cabello teñido sin afectar el color."),
                    Recomendacion("Graso", Nivel.Recomendado, "En cabellos grasos aplicar solo en las puntas para mejorar la definición, evitado incentivar el cebo en el cuero cabelludo."),
                    Recomendacion("Seco", Nivel.Recomendado, "Ideal para proporcionar hidratación intensa y revivir rizos en cabello secos."),
                    Recomendacion("Mixto", Nivel.Recomendado, "En cabellos mixtos utilizar en áreas específicamente secas para definir e hidratar sin afectar las áreas grasas."),
                    Recomendacion("Afro", Nivel.Recomendado, "Proporciona excelente definición y nutrición para cabellos afro, manteniendo los rizos y reduciendo el frizz."),
                    Recomendacion("Liso", Nivel.NoRecomendado, "No recomendado en cabellos lisos, está diseñado para cabellos rizados u ondulados."),
                    Recomendacion("Ondulado", Nivel.Recomendado, "Excelente para definir y enriquecer la textura natural de los cabellos ondulados."),
                    Recomendacion("Hidratacion", Nivel.Recomendado, "Complementa tratamientos de hidratación en cabellos ondulados, mejora la textura y reduce el frizz."),
                    Recomendacion("Alisado", Nivel.NoRecomendado, "No recomendado en cabellos con tratamientos alisantes, afectará al efecto del tratamiento."),
                    Recomendacion("Ondas", Nivel.Recomendado, "Ayuda a mantener y definir las ondas permanentes proporcionando nutrición e hidratación.")
                ),
                arrayListOf("Define y fortalece los rizos, manteniendo la hidratación y reduciendo el frizz con ácido hialurónico y betaína"),
                R.drawable.id8480000226310
            )
            registrarProducto(producto)
        }

        /*
        * Función para registrar nuevos productos
        * */
        private fun registrarProducto(producto: Producto) {
            productos.add(producto)
        }

        /*
        * Función para obtener un producto por su ID.
        *
        * Util para usar con el escaner y obtener información del producto desde su codigo de barras
        * */
        fun getProductoById(id: String): Producto? {
            return productos.find { it.id == id }
        }

        /*
        * Función para obtener todos los productos registrados
        * */

        fun getAllProductos(): List<Producto> {
            return productos
        }
    }
}