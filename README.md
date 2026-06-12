# 🎓 Sistema de Autogestión Estudiantil — Interfaz Gráfica

> 2° Instancia Evaluativa · Interfaz Gráfica · Java Swing + MVC + DAO

**Integrantes:** Bautista Pereiro · Facundo Degani · Agustín Barrera

---

## 🎨 Diseño en Figma — Prototipo inicial

> Las siguientes imágenes corresponden al prototipo visual diseñado en Figma antes de comenzar el desarrollo. No representan la interfaz final de la aplicación.

<div align="center">

<img width="480" alt="Mockup principal - Figma" src="https://github.com/user-attachments/assets/51a3c433-04d9-49b6-a7c8-1dcea6dffafe" />
&nbsp;&nbsp;&nbsp;
<img width="480" alt="Vista de materias - Figma" src="https://github.com/user-attachments/assets/acb7941c-9c9c-4cd4-8b50-1aa9d17ea409" />

</div>

> *(Agregar link al archivo de Figma)*

El diseño fue realizado antes de programar la interfaz, aplicando los principios de UX/UI vistos en clase: feedback inmediato, prevención de errores, visibilidad de estado y consistencia visual.

---

## 🖥️ Interfaz final — Aplicación real

<div align="center">

<img width="440" alt="Login - Registro de Estudiante" src="https://github.com/user-attachments/assets/8a34bc60-16e7-4b6f-83c8-66fdfbce5e27" />

<img width="1000" alt="Ventana principal - Sistema de Autogestión" src="https://github.com/user-attachments/assets/7198e8fd-da94-42a8-8db2-53af9733bb56" />

</div>

---

## 📋 Descripción

Transformación del Sistema de Autogestión Estudiantil desarrollado en la 1° IE (consola) a una aplicación de escritorio con interfaz gráfica en Java Swing, aplicando arquitectura **MVC + DAO** y persistencia en archivos de texto.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java JDK 25 | Lenguaje principal |
| Java Swing | Interfaz gráfica de escritorio |
| NetBeans / IntelliJ IDEA | IDEs de desarrollo |
| Figma | Diseño y prototipado de la UI |
| OpenCode | Asistente de código en terminal |
| Claude (Anthropic) | Auditoría de arquitectura, testing y corrección de bugs |
| Gemini (Google) | Consultas de implementación |

---

## 🏗️ Arquitectura — MVC + DAO

```
src/
├── main/           → Main.java — punto de entrada (SwingUtilities.invokeLater)
├── modelo/         → Estudiante, Materia, InscripcionMateria, Evaluable, Consultable
├── dao/            → EstudianteDAO, MateriaDAO, InscripcionDAO (I/O archivos .txt)
├── controlador/    → Controlador (lógica de negocio, sin imports javax.swing)
└── vista/          → VentanaPrincipal + paneles (solo delegan al Controlador)
```

**Regla principal:** la Vista nunca toca datos. El Controlador nunca toca Swing. El DAO nunca toca lógica de negocio.

---

## ✅ Funcionalidades implementadas

- **Perfil del estudiante** — nombre, carrera y año de ingreso visibles en la UI
- **Inscribir materia** — formulario con validaciones (código único, 3–10 chars, cuatrimestre 1 o 2)
- **Dar de baja** — con confirmación previa via `JOptionPane`
- **Registrar asistencia** — presente / ausente para la materia seleccionada; alerta si cae por debajo del 75%
- **Registrar nota** — validación rango 0–10, máximo 5 notas por materia
- **Tabla de materias** — columnas: Código, Nombre, Cuatrimestre, Año, Asistencia %, Promedio, Condición
- **Lista de alertas** — `JList` con materias en riesgo (asistencia 75%–85%)
- **Panel de reportes** — situación general, materias en riesgo y aprobadas (navegación via `CardLayout`)
- **Persistencia** — carga al iniciar, guarda después de cada operación en archivos `.txt`
- **Estado vacío** — mensaje visible cuando no hay materias inscriptas

---

## 🧩 Componentes Swing utilizados

`JTable` · `JList` · `JButton` · `JLabel` · `JMenuBar` · `JOptionPane` · `CardLayout` · `JScrollPane` · `BorderLayout` · `GridLayout` · `FlowLayout` · `BoxLayout`

---

## 💾 Persistencia

Archivos de texto plano con separación por `|`:

```
materias.txt       →  CODIGO|NOMBRE|CUATRIMESTRE|ANIO
inscripciones.txt  →  CODIGO|NOTA1,NOTA2|PRESENTES|TOTALES
```

Si los archivos no existen al iniciar, la app arranca con lista vacía sin lanzar ningún error.

---

## 🚀 Instrucciones de ejecución

**Requisitos:**
- JDK 25
- NetBeans 21+ o IntelliJ IDEA 2024+

**Pasos:**
1. Clonar el repositorio
2. Abrir el proyecto en NetBeans o IntelliJ
3. Ejecutar `Main.java`
4. Los archivos `.txt` de persistencia se generan automáticamente en la carpeta del proyecto

---

## 👥 Integrantes y roles

| Integrante | Rol |
|---|---|
| Bautista Pereiro | Diseño de interfaz en Figma · Testing · Corrección de bugs en Swing |
| Facundo Degani | *(completar rol)* |
| Agustín Barrera | *(completar rol)* |

---

## 🎥 Video explicativo

[![Ver video](https://img.shields.io/badge/Google%20Drive-Ver%20video-blue?logo=googledrive)](https://drive.google.com/file/d/1z0AEp7Wm_o7B0FEsvhOPYQrNweLAAvCk/view?usp=drive_link)

---

## 🤖 Uso de IA

| Integrante | Prompts |
|---|---|
| Bautista Pereiro | [Ver conversaciones](https://docs.google.com/document/d/19lqiD-Tn9syARZO47Xy8gbgD3hGGJpawiS4FDgW7oNI/edit?usp=drive_link) |
| Facundo Degani | [Conversación 1](https://g.co/gemini/share/47a271efe5c8) · [Conversación 2](https://g.co/gemini/share/df0181c9d892) |
| Agustín Barrera | [Ver conversaciones](https://docs.google.com/document/d/1ZfBFpKX05xw_Irz0OnFNQyXHp7DVQg3kuyl8SqWwLzA/edit?usp=drive_link) |

Las capturas individuales de cada integrante están en la carpeta `/docs/prompts/` del repositorio.

---

## ⚡ Desafíos encontrados

- Separación estricta MVC: evitar que la Vista construya objetos del Modelo
- Sincronización de `JTable` y `JList` después de cada operación
- Manejo del EDT (Event Dispatch Thread) para evitar crashes en Swing
- Persistencia robusta: arranque sin error cuando los `.txt` no existen
