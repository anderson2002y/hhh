/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ni.edu.uni.archivos.servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import ni.edu.uni.random.implement.ActivoFijoDaoImplement;
import ni.edu.uni.random.implement.EmpleadoDaoImplement;
import ni.edu.uni.random.main.Application;
import ni.edu.uni.random.pojo.ActivoFijo;
import ni.edu.uni.random.pojo.Empleado;
import ni.edu.uni.random.pojo.TipoActivoFijo;

/**
 *
 * @author yasser.membreno
 */
public class ActivoFijoService implements IServiceActivoFijo {
    private Scanner scan;
    private ActivoFijoDaoImplement afDao;
    private EmpleadoDaoImplement eDao;
    
    public ActivoFijoService(Scanner scan) throws IOException {
        this.scan = scan;
        afDao = new ActivoFijoDaoImplement();
        eDao = new EmpleadoDaoImplement();
    }

    @Override
    public ActivoFijo findById() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ActivoFijo[] findByClasificacion() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create() throws IOException {
        ActivoFijo activoFijo = null;
        
        int id, cantidad,clasificacion;
        String nombre, descripcion, fechaCompra;         
        double valor;
        
        
        System.out.println("codigo: ");
        id = scan.nextInt();
        scan.nextLine();
        System.out.println("Nombre: ");
        nombre = scan.nextLine();
        
        System.out.println("Descripcion: ");
        descripcion = scan.nextLine();
        
        System.out.println("Valor del activo: ");
        valor = scan.nextDouble();
        
        System.out.println("Cantidad: ");
        cantidad = scan.nextInt();
        
        do{
            //Application.subMenuTipoActivoFijo();
            clasificacion = scan.nextInt();
        }while(clasificacion < 1 || clasificacion > 4);
        
        System.out.println("Fecha de compra [dd/mm/yyyy]");
        fechaCompra = scan.next();
        //Leer Estado del activo fijo
        
        activoFijo = new ActivoFijo(nombre, descripcion, cantidad, TipoActivoFijo.values()[clasificacion - 1] , valor, 
                LocalDate.parse(fechaCompra, DateTimeFormatter.ofPattern("dd/M/yyyy")),ActivoFijo.EstadoActivoFijo.ACTIVO,null);

        afDao.create(activoFijo);
    }

    @Override
    public int update() throws IOException {
        int id, cantidad,clasificacion;
        String nombre, descripcion, fechaCompra;         
        double valor;
        
        System.out.println("Digite Id del activo a actualizar:");
        id = scan.nextInt();
        
        ActivoFijo af = afDao.findById(id);
        
        scan.nextLine();
        System.out.println("Nombre: ");
        nombre = scan.nextLine();
        
        System.out.println("Descripcion: ");
        descripcion = scan.nextLine();
        
        System.out.println("Valor del activo: ");
        valor = scan.nextDouble();
        
        System.out.println("Cantidad: ");
        cantidad = scan.nextInt();
        
        do{
            //Application.subMenuTipoActivoFijo();
            clasificacion = scan.nextInt();
        }while(clasificacion < 1 || clasificacion > 4);
        
        System.out.println("Fecha de compra [dd/mm/yyyy]");
        fechaCompra = scan.next();
        
        af.setNombre(nombre);
        af.setDescripcion(descripcion);
        af.setCantidad(cantidad);
        af.setValor(valor);
        af.setClasificacion(TipoActivoFijo.values()[clasificacion - 1]);
        af.setFechaCompra(LocalDate.parse(fechaCompra, DateTimeFormatter.ofPattern("dd/M/yyyy")));
        
        id =  afDao.update(af);
        
        return id;
        
    }

    @Override
    public boolean delete() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void findAll() throws IOException {
        System.out.format("%5s %20s %20s %20s %10s %10s %20s \n","Id","Nombre","Descripcion","Clasificacion","Valor","Cantidad","Fecha");
        for(ActivoFijo af : afDao.findAll()){
            Application.print(af);
        }
    }
    
    public void asignarActivoFijo() throws IOException{
        int id;
        
        System.out.println("Digite el Id del activo a asignar");
        id = scan.nextInt();
        
        ActivoFijo af = afDao.findById(id);
        if(af == null){
            System.out.format("Activo fijo con Id: %d no disponible\n",id);
            return;
        }
        
        if(af.getEmpleado() != null){
            System.out.format("El activo %s ya esta asignado!\n", af.getNombre());
            return;
        }
        
        System.out.println("Digite el Id del empleado");
        id = scan.nextInt();
        
        Empleado e = eDao.findById(id);
        if(e == null){
            System.out.format("Empleado con Id %d no existe\n",id);
            return;
        }
        
        afDao.asignarActivoFijo(af, e);
        
        System.out.format("Activo Fijo %s asignado a %s satisfactoriamente\n",
                af.getNombre(),e.getNombres() + " " + e.getApellidos());
    }
    
    public void findActivosFijosByEmpleado() throws IOException{
        
        System.out.println("Digite el Id del empleado");
        int id = scan.nextInt();
        
        Empleado e = eDao.findById(id);
        if(e == null){
            System.out.format("Empleado con Id %d no existe\n",id);
            return;
        }
        
        ActivoFijo activosFijos[] = afDao.findByEmpleado(e);
        
        if(activosFijos == null){
            System.out.format("El empleado %s no tiene activos fijos asignados\n", e.getNombres());
            return;
        }
        System.out.format("Empleado: %s\n", e.getNombres() + " " + e.getApellidos());
        System.out.println("Activos Fijos:");
        System.out.println("========================================");
        System.out.format("%5s %20s %20s %20s %10s %10s %20s \n","Id","Nombre","Descripcion","Clasificacion","Valor","Cantidad","Fecha");
        for(ActivoFijo af : activosFijos){
            Application.print(af);
        }
    }
}
