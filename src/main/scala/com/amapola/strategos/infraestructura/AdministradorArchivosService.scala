package com.amapola.strategos.infraestructura

import java.io.File
import java.nio.file.Files

trait AdministradorArchivosService {

  /**
    * Escribe el archivo dentro del destio especificado
    * @param archivo
    * @param destino
    */
  def crearArchivo(archivo: File, destino: String): Unit

}

object AdministradorArchivosServiceImpl extends AdministradorArchivosService {

  /**
    * Escribe el archivo dentro del destio especificado
    *
    * @param archivo
    * @param destino
    */
  override def crearArchivo(archivo: File, destino: String): Unit = {

    val directorio = new File(destino)

    val archivoFinalD = new File(directorio, archivo.getName)

    val archivoFinal = Files.newOutputStream(archivoFinalD.toPath)

    Files.copy(archivo.toPath, archivoFinal)

  }
}
