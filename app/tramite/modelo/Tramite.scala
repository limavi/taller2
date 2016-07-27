package tramite.modelo

case class Tramite(
    id: String,               //identificador
    Nombre:String,            //nombre del tramite
    Estado:String,            //estado del tramite (activo o inactivo)
    Descripcion:String,       //descripcion clara del tramite (a quien va dirigido, objetivo, etc)
    Pasos: List[Paso]         //Listados de pasos que se deben realizar para resolver el tramite
)
case class Paso(
    id:String,                //identificador
    Responsable:String,       //rol encargado de realizar el paso (ej:usuario, funcionarioAlcaldia)
    Secuencia:Int,            //indica el orden en el que se deben ejecutar el paso
    Estado:String,            //estado del tramite (activo o inactivo)
    Campos:List[campo]        //Listad de campos que se deben diligencia en el paso.Informacion a guardar.
)

case class campo(
    idComponente:String,        //identificador del componente. solo se puede usar componentes que hayamos definido
    nombre: String,             //Valor que se va a utilizar para identificar el campo en el js (ng_model)
    descripcionLabel: String,   //valor que se va a utilizar como label para indicar que solicita el campo
    atributos: List[atributo]   //Listado de atributos que sirven para configurar el componente (ej: min, max, placeholder)
)

case class atributo(
   nombreAtributo:String,       //nombre del atributo ej: min, max, placeholder
   valor:String                 //valor a asignar al atributo ej: min=10, placeholder="xxxx"
 )