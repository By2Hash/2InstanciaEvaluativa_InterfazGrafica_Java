---
name: swing-uxui-autogestor
description: >
  Skill para construir la interfaz Swing del Sistema de Autogestión Estudiantil
  siguiendo arquitectura MVC+DAO estricta, componentes obligatorios y buenas
  prácticas de UX/UI. Usar siempre que se pida crear, modificar o completar
  cualquier parte de la vista Swing del proyecto de autogestión estudiantil,
  incluyendo JFrame, paneles, tablas, listas, menús, layouts o cualquier
  componente gráfico del sistema. También aplicar cuando se pida revisar
  que el código Swing no viola la separación MVC, o cuando se necesite
  implementar CardLayout, JTable, JList, JMenuBar, JOptionPane o persistencia.
---

# Skill: Swing UX/UI — Sistema de Autogestión Estudiantil

## Contexto del Proyecto

**App:** Sistema de Autogestión Estudiantil (Java Swing, NetBeans)  
**Arquitectura:** MVC + DAO estricta  
**Persistencia:** Archivos .txt (FileWriter/BufferedReader) o Serialización  
**Entrega:** 12 junio 2026  

### Paquetes obligatorios
```
src/
├── modelo/        → Estudiante, Materia, InscripcionMateria, Evaluable, Consultable
├── dao/           → EstudianteDAO, MateriaDAO, InscripcionDAO (solo I/O archivos)
├── controlador/   → MainControlador (sin imports javax.swing)
└── vista/         → MainFrame + paneles (solo llaman al Controlador)
```

---

## REGLAS CRÍTICAS DE ARQUITECTURA (no violar nunca)

```
❌ PROHIBIDO en la Vista:        new Materia(...), validaciones, FileWriter/Reader
❌ PROHIBIDO en el DAO:          JTable, JTextField, cualquier import javax.swing
❌ PROHIBIDO en el Controlador:  imports javax.swing, JOptionPane, código visual
✅ ActionListeners → solo llaman métodos del Controlador
✅ El Controlador → llama al Modelo y DAO, luego actualiza la Vista via métodos
✅ El DAO → solo lee/escribe archivos, sin lógica de negocio
```

### Patrón correcto de comunicación Vista ↔ Controlador

```java
// EN LA VISTA — solo delega, nunca procesa
btnInscribir.addActionListener(e -> controlador.inscribirMateria(
    txtNombre.getText(), txtCodigo.getText(),
    (Integer) spnCuatrimestre.getValue(), (Integer) spnAnio.getValue()
));

// EN EL CONTROLADOR — lógica completa, sin Swing
public void inscribirMateria(String nombre, String codigo, int cuatri, int anio) {
    // 1. Validar
    if (codigo.length() < 3 || codigo.length() > 10) {
        vista.mostrarError("El código debe tener entre 3 y 10 caracteres.");
        return;
    }
    // 2. Crear y agregar al modelo
    Materia m = new Materia(nombre, codigo, cuatri, anio);
    estudiante.inscribirMateria(m);
    // 3. Persistir
    dao.guardar(estudiante);
    // 4. Actualizar vista
    vista.actualizarTabla(estudiante.getMaterias());
    vista.actualizarAlertas(estudiante.getMateriasCriticas());
}
```

---

## COMPONENTES SWING OBLIGATORIOS

Leer `references/componentes.md` para implementación detallada de cada uno.

| Componente   | Función en el sistema                                              |
|-------------|---------------------------------------------------------------------|
| JMenuBar    | Archivo (Cerrar) + Reportes (Situación general, En riesgo, Aprobadas) |
| JTable      | Materias con columnas: Código, Nombre, Cuatrimestre, Año, Asistencia%, Promedio, Condición |
| JList       | Alertas de asistencia entre 75%–85%                                |
| JButton     | Inscribir, Dar de baja, Registrar Asistencia (Presente/Ausente), Registrar Nota |
| JLabel      | Perfil, títulos de sección, mensajes de estado/feedback            |
| JOptionPane | Confirmación de baja + alerta cuando asistencia < 75%             |
| CardLayout  | Navegar entre panel principal y panel de reportes                  |
| JScrollPane | Envuelve JTable y JList                                            |

---

## LAYOUTS

```java
// Estructura principal del JFrame
mainFrame.setLayout(new BorderLayout());
mainFrame.add(menuBar, BorderLayout.NORTH);      // JMenuBar arriba
mainFrame.add(panelIzquierdo, BorderLayout.WEST); // Perfil + Inscripción
mainFrame.add(panelCentral, BorderLayout.CENTER); // CardLayout (tabla + reportes)

// Panel izquierdo — formularios
panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

// Sección de inscripción — GridLayout para labels y campos alineados
panelInscripcion.setLayout(new GridLayout(0, 2, 5, 5));

// Acciones en la parte inferior — FlowLayout
panelAcciones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
```

---

## DISEÑO VISUAL (UX/UI)

Ver `references/diseno.md` para guía completa de colores, tipografía y estilo.

### Paleta de colores (basada en el mockup de referencia)
```java
// Colores principales
Color FONDO_APP        = new Color(192, 192, 192);  // gris medio — fondo general
Color FONDO_PANEL      = new Color(211, 211, 211);  // gris claro — paneles internos
Color ACENTO_PRIMARIO  = new Color(0, 0, 200);      // azul — botón principal, fila seleccionada
Color ALERTA_RIESGO    = new Color(255, 255, 100);  // amarillo — ítems en JList de alertas
Color FILA_SELECCION   = new Color(100, 100, 255);  // azul medio — fila activa en tabla
Color TEXTO_NORMAL     = Color.BLACK;
Color BORDE_PANEL      = Color.DARK_GRAY;
```

### Principios UX aplicados al sistema
1. **Feedback inmediato** — la tabla y la JList se actualizan en el mismo método después de cada acción
2. **Prevención de errores** — validar antes de crear objetos; mostrar mensajes claros con JOptionPane
3. **Visibilidad de estado** — condición (Regular/Libre/Aprobada) resaltada en la tabla con color de fila
4. **Consistencia** — todos los botones de acción tienen el mismo tamaño y están en el panel Acciones
5. **Estado vacío** — cuando no hay materias, mostrar JLabel con mensaje en la tabla

---

## PERSISTENCIA

```java
// DAO — texto plano (opción recomendada)
// materias.txt → una línea por materia: CODIGO|NOMBRE|CUATRIMESTRE|ANIO
// inscripciones.txt → CODIGO|NOTA1,NOTA2|PRESENTES|TOTALES

public class MateriaDAO {
    private static final String ARCHIVO = "materias.txt";

    public List<Materia> cargar() {
        List<Materia> lista = new ArrayList<>();
        File f = new File(ARCHIVO);
        if (!f.exists()) return lista;  // ← arrancar con lista vacía sin error
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Materia.fromTexto(linea));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return lista;
    }

    public void guardar(List<Materia> materias) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Materia m : materias) bw.write(m.toTexto() + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }
}
```

---

## CARDLAYOUT — NAVEGACIÓN

```java
// En la Vista
private CardLayout cardLayout = new CardLayout();
private JPanel panelCards = new JPanel(cardLayout);

// Agregar paneles
panelCards.add(panelPrincipal, "PRINCIPAL");
panelCards.add(panelReportes, "REPORTES");

// Cambiar de panel (desde ActionListener que llama al controlador)
public void mostrarPanel(String nombre) {
    cardLayout.show(panelCards, nombre);
}

// Menú Reportes → llama controlador.mostrarReportes()
// Controlador llama → vista.mostrarPanel("REPORTES")
```

---

## JTABLE — IMPLEMENTACIÓN CORRECTA

```java
// Modelo de tabla no editable directamente
String[] columnas = {"Código","Nombre","Cuatrimestre","Año","Asistencia %","Promedio","Condición"};
DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
    @Override public boolean isCellEditable(int r, int c) { return false; }
};
JTable tabla = new JTable(modeloTabla);
tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
tabla.getTableHeader().setReorderingAllowed(false);

// Método en Vista para actualizar la tabla (llamado desde Controlador)
public void actualizarTabla(List<InscripcionMateria> inscripciones) {
    modeloTabla.setRowCount(0); // limpiar
    if (inscripciones.isEmpty()) {
        // estado vacío
        modeloTabla.addRow(new Object[]{"—","Sin materias inscriptas","—","—","—","—","—"});
        return;
    }
    for (InscripcionMateria im : inscripciones) {
        Materia m = im.getMateria();
        modeloTabla.addRow(new Object[]{
            m.getCodigo(), m.getNombre(), m.getCuatrimestre(), m.getAnio(),
            String.format("%.0f%%", im.getPorcentajeAsistencia()),
            String.format("%.1f", im.getPromedio()),
            im.getCondicion()
        });
    }
}
```

---

## FLUJOS FUNCIONALES PRINCIPALES

### 1. Inscribir materia
```
Vista: btnInscribir click
  → controlador.inscribirMateria(nombre, codigo, cuatri, anio)
    → validar (codigo único, largo 3-10, cuatri 1 o 2)
    → new Materia + estudiante.inscribirMateria()
    → dao.guardar(estudiante)
    → vista.actualizarTabla() + vista.actualizarAlertas()
    → vista.limpiarFormulario()
```

### 2. Dar de baja
```
Vista: btnBaja click
  → controlador.darDeBaja(codigoSeleccionado)
    → JOptionPane.showConfirmDialog desde la VISTA (no desde controlador)
      Si confirma → controlador.confirmarBaja(codigo)
        → estudiante.darDeBaja(codigo)
        → dao.guardar(estudiante)
        → vista.actualizarTabla() + vista.actualizarAlertas()
```

### 3. Registrar asistencia
```
Vista: btnPresente / btnAusente click
  → controlador.registrarAsistencia(codigoSeleccionado, esPresente)
    → inscripcion.registrarAsistencia(esPresente)
    → dao.guardar(estudiante)
    → vista.actualizarTabla()
    → si asistencia < 75% → vista.mostrarAlertaAsistencia(codigo)
```

### 4. Registrar nota
```
Vista: btnAgregarNota click
  → controlador.registrarNota(codigoSeleccionado, nota)
    → validar: 0 ≤ nota ≤ 10, cantidad < 5
    → inscripcion.agregarNota(nota)
    → dao.guardar(estudiante)
    → vista.actualizarTabla()
```

---

## CHECKLIST ANTES DE ENTREGAR

Leer `references/checklist.md` para la lista completa de verificación.

### Quick check (los más críticos):
- [ ] Sin `FileWriter`/`FileReader` en ningún ActionListener ni en la Vista
- [ ] Sin `new Materia(...)` ni validaciones dentro de la Vista  
- [ ] Sin imports `javax.swing` en el Controlador
- [ ] El DAO no referencia ningún componente Swing
- [ ] CardLayout funciona con al menos 2 paneles navegables
- [ ] JList muestra solo materias con asistencia 75%–85% (no las < 75%)
- [ ] JScrollPane envuelve la JTable y la JList
- [ ] JOptionPane aparece antes de dar de baja
- [ ] Archivos .txt se crean en la carpeta del proyecto (sin rutas absolutas)
- [ ] Si el .txt no existe, la app inicia sin error con lista vacía


---------------------------------

# Checklist de Entrega — Autogestión Estudiantil Swing

## 🏗️ Arquitectura MVC+DAO

- [ ] Paquetes separados: `modelo`, `dao`, `controlador`, `vista`
- [ ] **Vista:** cero validaciones, cero new Materia(), cero FileWriter/Reader
- [ ] **DAO:** cero imports javax.swing, cero referencias a JTable/JTextField
- [ ] **Controlador:** cero imports javax.swing, cero JOptionPane
- [ ] Los ActionListeners de la Vista solo llaman métodos del Controlador
- [ ] El Controlador actualiza la Vista solo a través de métodos públicos de la Vista
- [ ] El DAO solo es llamado desde el Controlador

## 📦 Componentes obligatorios presentes y funcionales

- [ ] `JMenuBar` con menú Archivo (Cerrar) y Reportes (al menos 1 ítem funcional)
- [ ] `JTable` muestra materias con al menos: Código, Nombre, Asistencia%, Promedio, Condición
- [ ] `JList` muestra materias con asistencia entre 75% y 85% (solo ese rango)
- [ ] `JButton` — al menos 3 funcionales: Inscribir, Dar de baja, Registrar asistencia
- [ ] `JOptionPane` — aparece al dar de baja (confirmación) y al bajar de 75% (alerta)
- [ ] `CardLayout` — navegación funcional entre al menos 2 paneles
- [ ] `JScrollPane` — envuelve la JTable
- [ ] `JScrollPane` — envuelve la JList
- [ ] `BorderLayout` como layout principal del JFrame
- [ ] Al menos un layout adicional (GridLayout, BoxLayout, FlowLayout, GridBagLayout) en algún panel

## 💾 Persistencia

- [ ] Al menos 1 archivo .txt creado en la carpeta del proyecto (sin rutas absolutas)
- [ ] Los datos se cargan al arrancar la app (en el constructor del Controlador)
- [ ] Los datos se guardan después de cada inscripción, baja, asistencia o nota
- [ ] Si el archivo no existe al iniciar → la app abre sin error, con lista vacía
- [ ] Los archivos NO usan rutas absolutas (ej: `"materias.txt"` no `"C:/Users/..."`)

## ✅ Funcionalidades mínimas

- [ ] Perfil del estudiante visible (nombre, carrera, año de ingreso)
- [ ] Inscribir materia: formulario completo + validaciones (código único, largo 3-10, cuatrimestre 1 o 2)
- [ ] Dar de baja: elimina la materia seleccionada, con confirmación previa
- [ ] Registrar asistencia: Presente/Ausente para materia seleccionada
- [ ] Registrar nota: rango 0-10, máximo 5 notas por materia
- [ ] Tabla actualiza en tiempo real tras cada acción
- [ ] JList de alertas actualiza junto con la tabla
- [ ] Estado vacío: mensaje cuando no hay materias
- [ ] Al menos un reporte accesible desde el menú Reportes

## 🎨 UX/UI

- [ ] Colores consistentes entre todos los paneles
- [ ] Botón principal (Inscribir) visualmente diferenciado (color acento)
- [ ] Botón destructivo (Dar de baja) diferenciado (color rojo/advertencia)
- [ ] Mensajes de error claros con JOptionPane o JLabel
- [ ] Fila seleccionada en la tabla tiene color visible
- [ ] JList de alertas tiene fondo amarillo (o color diferenciador)
- [ ] Secciones con TitledBorder o separación visual clara

## 📁 Entregables del proyecto

- [ ] Repositorio GitHub con commits de TODOS los integrantes
- [ ] Video 10-15 min (cámara + pantalla con código)
- [ ] Capturas de prompts de IA en carpeta del proyecto (o README con links)
- [ ] Link a Figma en el repositorio o README
- [ ] README.md con instrucciones de ejecución (recomendado)

## ⚠️ Errores que anulan la IE (verificar primero)

```
❌ FileWriter o FileReader dentro de un ActionListener → ANULA arquitectura
❌ new Materia(...) dentro de la Vista → ANULA arquitectura  
❌ JTable o JTextField en el DAO → ANULA arquitectura
❌ imports javax.swing en el Controlador → ANULA arquitectura
❌ Falta el repositorio, el video o la documentación → ANULA la instancia
❌ Integrante sin commits → pierde 50% de la nota grupal
```

## 🎯 BONUS (si el tiempo lo permite)

- [ ] DAO JDBC con MySQL (tablas, Connection, PreparedStatement, ResultSet)
- [ ] Edición de registros (seleccionar fila → cargar en formulario → Guardar cambios)
- [ ] Prototipo navegable en Figma (pantallas conectadas con interacciones)
- [ ] Búsqueda de materia por código o nombre (resultado resaltado en tabla)
- [ ] Reporte de materias en riesgo ordenado por asistencia ascendente
- [ ] Reporte de aprobadas con nota máx, mín y promedio del conjunto
- [ ] README.md completo con estructura, integrantes, roles y desafíos

-------------------------------------------

# Referencia: Componentes Swing — Implementación Detallada

## JMenuBar

```java
JMenuBar menuBar = new JMenuBar();

// Menú Archivo
JMenu menuArchivo = new JMenu("Archivo");
JMenuItem itemCerrar = new JMenuItem("Cerrar");
itemCerrar.addActionListener(e -> System.exit(0));
menuArchivo.add(itemCerrar);

// Menú Reportes
JMenu menuReportes = new JMenu("Reportes");
JMenuItem itemSituacion  = new JMenuItem("Situación general");
JMenuItem itemRiesgo     = new JMenuItem("Materias en riesgo");
JMenuItem itemAprobadas  = new JMenuItem("Materias aprobadas");
itemSituacion.addActionListener(e -> controlador.mostrarReporteSituacion());
itemRiesgo.addActionListener(e -> controlador.mostrarReporteRiesgo());
itemAprobadas.addActionListener(e -> controlador.mostrarReporteAprobadas());
menuReportes.add(itemSituacion);
menuReportes.add(itemRiesgo);
menuReportes.add(itemAprobadas);

// Menú Ayuda
JMenu menuAyuda = new JMenu("Ayuda");

menuBar.add(menuArchivo);
menuBar.add(menuReportes);
menuBar.add(menuAyuda);
frame.setJMenuBar(menuBar);
```

---

## JTable dentro de JScrollPane

```java
// Crear modelo no editable
String[] columnas = {"Código","Nombre","Cuatrimestre","Año","Asistencia %","Promedio","Condición"};
DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
    @Override
    public boolean isCellEditable(int row, int col) { return false; }
};

JTable tabla = new JTable(modeloTabla);
tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
tabla.setRowHeight(22);
tabla.getTableHeader().setReorderingAllowed(false);
tabla.setFillsViewportHeight(true);

// Renderer para colorear la fila seleccionada y la condición
tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(
            JTable t, Object val, boolean sel, boolean foc, int row, int col) {
        super.getTableCellRendererComponent(t, val, sel, foc, row, col);
        if (sel) {
            setBackground(new Color(100, 100, 255));
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }
        return this;
    }
});

// Envolver en JScrollPane
JScrollPane scrollTabla = new JScrollPane(tabla);
scrollTabla.setPreferredSize(new Dimension(500, 200));
```

---

## JList con alertas (DefaultListModel)

```java
DefaultListModel<String> modeloAlertas = new DefaultListModel<>();
JList<String> listaAlertas = new JList<>(modeloAlertas);
listaAlertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

// Renderer con fondo amarillo para alertas
listaAlertas.setCellRenderer(new DefaultListCellRenderer() {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean sel, boolean foc) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, sel, foc);
        label.setBackground(new Color(255, 255, 100)); // amarillo
        label.setOpaque(true);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
});

JScrollPane scrollAlertas = new JScrollPane(listaAlertas);

// Actualizar desde la Vista (llamado por Controlador)
public void actualizarAlertas(List<InscripcionMateria> criticas) {
    modeloAlertas.clear();
    for (InscripcionMateria im : criticas) {
        modeloAlertas.addElement(
            im.getMateria().getNombre() + " - " +
            String.format("%.0f%%", im.getPorcentajeAsistencia())
        );
    }
}
```

---

## JOptionPane — Baja y Alertas

```java
// Confirmación de baja (llamar desde ActionListener en la Vista)
int confirm = JOptionPane.showConfirmDialog(
    this,
    "¿Eliminar materia " + codigoSeleccionado + "?\nEsta acción no se puede deshacer.",
    "Confirmar Baja",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.WARNING_MESSAGE
);
if (confirm == JOptionPane.YES_OPTION) {
    controlador.confirmarBaja(codigoSeleccionado);
}

// Alerta de asistencia crítica (método público en Vista, llamado por Controlador)
public void mostrarAlertaAsistencia(String nombreMateria, double porcentaje) {
    JOptionPane.showMessageDialog(
        this,
        "⚠️ Asistencia crítica en " + nombreMateria + ": " +
        String.format("%.0f%%", porcentaje) + "\nRiesgo de perder la regularidad.",
        "Alerta de Asistencia",
        JOptionPane.WARNING_MESSAGE
    );
}

// Error genérico
public void mostrarError(String mensaje) {
    JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
}

// Mensaje de reporte (desde menú)
public void mostrarReporte(String titulo, String contenido) {
    JOptionPane.showMessageDialog(this, contenido, titulo, JOptionPane.INFORMATION_MESSAGE);
}
```

---

## JSpinner para Cuatrimestre y Año

```java
// Cuatrimestre — solo 1 o 2
SpinnerNumberModel modeloCuatri = new SpinnerNumberModel(1, 1, 2, 1);
JSpinner spnCuatrimestre = new JSpinner(modeloCuatri);

// Año — rango razonable
SpinnerNumberModel modeloAnio = new SpinnerNumberModel(2024, 2000, 2030, 1);
JSpinner spnAnio = new JSpinner(modeloAnio);
// Quitar separador de miles
JSpinner.NumberEditor editorAnio = new JSpinner.NumberEditor(spnAnio, "#");
spnAnio.setEditor(editorAnio);
```

---

## JTextField con validación visual

```java
JTextField txtCodigo = new JTextField();
txtCodigo.setToolTipText("3 a 10 caracteres, único");

// Hint/placeholder (Java puro sin librerías)
txtCodigo.setForeground(Color.GRAY);
txtCodigo.setText("Ej: MAT101");
txtCodigo.addFocusListener(new FocusAdapter() {
    @Override public void focusGained(FocusEvent e) {
        if (txtCodigo.getText().equals("Ej: MAT101")) {
            txtCodigo.setText(""); txtCodigo.setForeground(Color.BLACK);
        }
    }
    @Override public void focusLost(FocusEvent e) {
        if (txtCodigo.getText().isEmpty()) {
            txtCodigo.setForeground(Color.GRAY); txtCodigo.setText("Ej: MAT101");
        }
    }
});
```

---

## Botones de Radio para Asistencia

```java
ButtonGroup grupoAsistencia = new ButtonGroup();
JRadioButton rbPresente = new JRadioButton("Presente");
JRadioButton rbAusente  = new JRadioButton("Ausente");
rbPresente.setSelected(true);
grupoAsistencia.add(rbPresente);
grupoAsistencia.add(rbAusente);

// Usarlos en el ActionListener del botón registrar
btnRegistrarAsistencia.addActionListener(e -> {
    String codigoSel = obtenerCodigoSeleccionado(); // helper que lee la fila seleccionada
    if (codigoSel == null) { mostrarError("Seleccioná una materia primero."); return; }
    controlador.registrarAsistencia(codigoSel, rbPresente.isSelected());
});
```

---

## Helper: obtener fila seleccionada de la tabla

```java
// En la Vista — método reutilizable
private String obtenerCodigoSeleccionado() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) return null;
    return (String) modeloTabla.getValueAt(fila, 0); // columna 0 = Código
}
```


--------------------------------------------

# Referencia: Diseño UX/UI — Autogestión Estudiantil

## Paleta de Colores

```java
// Definir como constantes en la Vista o en una clase Tema
public class Tema {
    public static final Color FONDO_APP         = new Color(192, 192, 192);
    public static final Color FONDO_PANEL       = new Color(211, 211, 211);
    public static final Color FONDO_FORMULARIO  = new Color(230, 230, 230);
    public static final Color ACENTO_PRIMARIO   = new Color(0,   0,   200); // azul botón inscribir
    public static final Color FILA_SELECCION    = new Color(100, 100, 255); // fila activa tabla
    public static final Color ALERTA_AMARILLO   = new Color(255, 255, 100); // JList alertas
    public static final Color FONDO_ACCIONES    = new Color(180, 180, 180); // panel inferior
    public static final Color TEXTO_NORMAL      = Color.BLACK;
    public static final Color TEXTO_INVERSO     = Color.WHITE;
    public static final Color BORDE_SECCION     = Color.DARK_GRAY;
}
```

## Tipografía

```java
// Títulos de sección (ej: "Perfil del Estudiante", "Acciones")
Font fuenteTitulo   = new Font("SansSerif", Font.BOLD,  12);
// Etiquetas de campo
Font fuenteEtiqueta = new Font("SansSerif", Font.PLAIN, 11);
// Contenido de tabla
Font fuenteTabla    = new Font("Monospaced", Font.PLAIN, 11);
// Botón principal
Font fuenteBoton    = new Font("SansSerif", Font.BOLD,  11);
```

## Estructura del JFrame (basada en el mockup)

```
┌────────────────────────────────────────────────────┐
│ Sistema de Autogestión Estudiantil     [_][□][X]   │
├──────────────────────────────────────────────────  │
│  Archivo │ Reportes │ Ayuda                        │  ← JMenuBar
├──────────────┬─────────────────────────────────────┤
│              │  Tabla con las materias              │
│ Perfil del   │ ┌──────────────────────────────────┐│
│ Estudiante   │ │ Código│Nombre│Cuatri│Año│Asist%│..││  ← JTable en JScrollPane
│              │ └──────────────────────────────────┘│
│ Nombre: [  ] │                                     │
│ Carrera: [ ] │                                     │
│ Año Ing: [ ] │                                     │
├──────────────│─────────────────────────────────────┤
│ Inscripción  │  Acciones                           │
│ Nombre: [  ] │ ┌──────────┬──────────┬───────────┐ │
│ Código: [  ] │ │Asistencia│  Notas   │  Dar Baja │ │  ← 3 paneles de acción
│ Cuatr: [↕]  │ │ ● Presente│ Nota:[  ]│           │ │
│ Año:   [↕]  │ │ ○ Ausente │[Agregar] │[Dar Baja] │ │
│ [Inscribir]  │ └──────────┴──────────┴───────────┘ │
├──────────────│─────────────────────────────────────┤
│ Alertas (75–85%)                                   │  ← JList en JScrollPane
│ ┌──────────────────────────────────────────────┐   │
│ │ Interfaz Gráfica - 80%                       │   │
│ │ Estructura de Datos - 45%                    │   │
│ └──────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────┘
```

## Aplicar colores a paneles

```java
// Fondo general del frame
frame.getContentPane().setBackground(Tema.FONDO_APP);

// Crear panel con título de sección (TitledBorder)
private JPanel crearPanelSeccion(String titulo) {
    JPanel panel = new JPanel();
    panel.setBackground(Tema.FONDO_PANEL);
    panel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Tema.BORDE_SECCION, 1),
        titulo,
        TitledBorder.LEFT,
        TitledBorder.TOP,
        new Font("SansSerif", Font.BOLD, 11),
        Tema.TEXTO_NORMAL
    ));
    return panel;
}

// Botón principal (Inscribir)
private JButton crearBotonPrimario(String texto) {
    JButton btn = new JButton(texto);
    btn.setBackground(Tema.ACENTO_PRIMARIO);
    btn.setForeground(Tema.TEXTO_INVERSO);
    btn.setFont(new Font("SansSerif", Font.BOLD, 11));
    btn.setFocusPainted(false);
    return btn;
}

// Botón peligroso (Dar de Baja)
private JButton crearBotonPeligro(String texto) {
    JButton btn = new JButton(texto);
    btn.setBackground(new Color(180, 0, 0));
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font("SansSerif", Font.BOLD, 11));
    btn.setFocusPainted(false);
    return btn;
}
```

## Principios UX — Justificación para el video/entrega

| Principio         | Implementación en el proyecto                                              |
|-------------------|----------------------------------------------------------------------------|
| Feedback inmediato | Tabla y JList se actualizan en tiempo real tras cada acción               |
| Prevención errores | Spinner para cuatrimestre (solo 1 o 2), validaciones en controlador       |
| Visibilidad estado | Condición (Regular/Libre/Aprobada) visible en cada fila de la tabla       |
| Jerarquía visual  | Títulos de sección con TitledBorder, botón primario resaltado en azul      |
| Consistencia      | Misma estructura de panel para todas las secciones                         |
| Alertas claras    | JList con fondo amarillo para asistencia en riesgo; JOptionPane para <75%  |
| Estado vacío      | Mensaje "Sin materias inscriptas" cuando la tabla está vacía               |
| Confirmación      | JOptionPane antes de acciones destructivas (dar de baja)                   |

## Tamaños recomendados

```java
frame.setSize(800, 620);
frame.setMinimumSize(new Dimension(700, 550));
frame.setLocationRelativeTo(null); // centrar en pantalla

// Panel izquierdo
panelIzquierdo.setPreferredSize(new Dimension(200, 0));

// Tabla — alto mínimo
scrollTabla.setPreferredSize(new Dimension(0, 180));

// Alertas JList
scrollAlertas.setPreferredSize(new Dimension(0, 80));
```

## Inicialización correcta del JFrame

```java
public class MainFrame extends JFrame {
    public MainFrame(MainControlador controlador) {
        super("Sistema de Autogestión Estudiantil");
        this.controlador = controlador;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponentes();
        initLayouts();
        initListeners();
        pack(); // o setSize(800, 620)
        setLocationRelativeTo(null);
    }
    
    private void initComponentes() { /* crear todos los componentes */ }
    private void initLayouts()     { /* armar estructura de paneles */ }
    private void initListeners()   { /* agregar ActionListeners */ }
}
```