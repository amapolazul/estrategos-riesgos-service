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

    val archivoActual = Files.newInputStream(archivo.toPath)
    val archivoFinal = Files.newOutputStream(archivoFinalD.toPath)

    val buffer = new Array[Byte](1024)
    var bytesRead = 0

    while ({
      (bytesRead = archivoActual.read(buffer)) != -1
    }) archivoFinal.write(buffer, 0, bytesRead)
    archivoActual.close
    archivoFinal.flush
  }
}
