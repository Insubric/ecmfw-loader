
package ch.wsl.data.app

import java.io._

import org.rogach.scallop._
import org.ecmwf._
import org.json._

// run -p "165.128" -a "70/-130/30/-60"
// run -p "165.128" -a "70/-130/30/-60" -t "12" -q "2013-01-01/to/2013-01-31" -f "netcdf" -o "data.nc"
// run -d "interim" -s "oper" -l "sfc" -w "0" -p "165.128" -q "2013-01-01/to/2013-01-31" -g "0.75/0.75" -a "70/-130/30/-60" -t "12" -f "netcdf" -o "data.nc"
// run -d "tigge" -s "oper" -l "sl" -w "24/to/120/by/24" -p "tp" -q "20071001/to/20071003" -g "0.75/0.75" -a "70/-130/30/-60" -t "00/12" -f "netcdf" -o "data.nc"


class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
    
    version("ecmwf-loader: Version 1.1 (c) 2016 WSL: Boris Pezzatti")
    banner("""
             |This application: - retrieves data from ecmwf data server.
             |                    ERA-Interim is a global reanalysis of recorded climate observations over the past 3.5 decades.
             |                    It is presented as a gridded data set at approximately 0.7 degrees spatial resolution, and 37 atmospheric levels.
             |                    ERA-Interim is produced by the European Centre for Medium-Range Weather Forecasts (ECMWF).
             |
             |Options:
             |""".stripMargin)
    footer("\n\n\n"+"""EXAMPLES:
             |
             |#java -jar ecmfw.jar -d "interim" -s "oper" -l "sfc" -w "0" -p "165.128" -q "2013-01-01/to/2013-01-31" -g "0.75/0.75" -a "70/-130/30/-60" -t "12" -f "netcdf" -o "data.nc"
             |
          """.stripMargin)
  
  
//ERA-Interim is available with a time resolution of 6 hours (0000, 0600, 1200 and 1800 UTC),
//has a spectral T255 horizontal resolution (79 km) and 60 model levels with the top of the atmosphere at 0.1 hPa.

  
  
//         The "dataset" parameter is one of: interim = ECMWF Global Reanalysis Data - ERA Interim (Jan 1979 - present)
//         ERA-Interim is a global reanalysis of recorded climate observations over the past 3.5 decades.
//         It is presented as a gridded data set at approximately 0.7 degrees spatial resolution, and 37 atmospheric levels.
//         ERA-Interim is produced by the European Centre for Medium-Range Weather Forecasts (ECMWF).  
    val dataset = opt[String](descr="Dataset: interim = ECMWF Global Reanalysis Data - ERA Interim (Jan 1979 - present)", required=true, default=Some("interim"))
    
    //         originating forecasting system(oper, wave, enfo, seas, ...) oper=operational Atmospheric model
    val stream = opt[String](descr="Originating forecasting system (oper, wave, enfo, seas, ...). [oper=operational Atmospheric model]", required=true, default=Some("oper"))
    val number = opt[String](descr="Number", required=true, default=Some("all"))
    val origin = opt[String](short='u', descr="Origin", required=true, default=Some("all"))
    
//         meteorological parameter: can be specified in various ways (i.e. t, temperature, 130, 30.128)
//         relative humidity    short name=r     paramID=157   oppure 157.128
//         wind speed           short name=ws    paramID=10
//         10 metre wind speed  short name=10si  paramID=207
    val param = opt[String](descr="Meteorological parameter: can be specified in various ways, i.e. temperature[t, temperature, 130, 130.128], " +
                            "relative humidity[r, 157, 157.128],  wind speed[ws, 10],  10-metre wind speed[10si, 207]", required=true)
    
//         type of level (pl, ml, sfc, pt, pv) pressure level=pl, model level=ml, surface=sfc
    val levtype = opt[String](descr="Type of level: pl (pressure level), ml (model level), sfc (surface), pt, pv", required=true, default=Some("sfc"))
    
//         se type of level = pl (pressure level) allora si deve definire il livello (e.g. 1000 hPa)
    val levelist = opt[String](short='i',descr="Specification for level type: pl (pressure level)", required=false)
    
//         If step 0 is chosen, then only analysed fields, which are produced for 0000, 0600, 1200 and 1800 UTC, are available.
//         If step 3, 6, 9 or 12 is selected then only forecast fields which are produced from forecasts beginning at 0000 and 1200 UTC, are available.
    val step = opt[String](short='w', descr="If step 0 is chosen, then only analysed fields, which are produced for 0000, 0600, 1200 and 1800 UTC, are available. " +
         "If step 3, 6, 9 or 12 is selected then only forecast fields which are produced from forecasts beginning at 0000 and 1200 UTC, are available.", required=true, default=Some("0"))
    
//         Grid resolution in degrees: 0.125/0.125; 0.25/0.25; 0.5/0.5; 0.75/0.75; 1/1; 1.125/1.125; 1.5/1.5; 2/2; 2.5/2.5; 3/3      
    val grid = opt[String](descr="Grid resolution in degrees: 0.125/0.125; 0.25/0.25; 0.5/0.5; 0.75/0.75; 1/1; 1.125/1.125; 1.5/1.5; 2/2; 2.5/2.5; 3/3",required=true, default=Some("0.75/0.75"))
    
//         Time: 00:00:00; 06:00:00; 12:00:00; 18:00:00
    val time = opt[String](descr="Time: 00:00:00; 06:00:00; 12:00:00; 18:00:00", required=true, default=Some("12"))
    val date = opt[String](short='q', descr="Date, e.g. 2013-01-01/to/2013-01-31",required=true, default=Some("2013-01-01/to/2013-01-31"))
    
//         type of field (an, fc, ...) Forecast=fc, Analysis=an       
    val xtype = opt[String](descr="type", default=Some("an"))
    
//         ECMWF classification (od, rd, e4, ...)
    val yclass = opt[String](descr="ECMWF classification (od, rd, e4, ...)", default=Some("ei"))
    
//         Area limits in degrees: Nord/West/Sud/Est      
    val area = opt[String](descr="Area limits in degrees: Nord/West/Sud/Est ",required=true)
    val format = opt[String](descr="Output format", default=Some("netcdf"))
    val target = opt[String](short='o', descr="Target file name",required=true, default=Some("data.nc"))

}


object ConsoleApp  { 
    
    def main(args: Array[String]) {
        
        val x = new Conf(args)
        
        try{
            if (x.param.supplied && x.time.supplied && x.area.supplied){

                val server = new DataServer() 

                val request = new JSONObject()

                request.put("dataset" , x.dataset())
                request.put("stream" , x.stream())
                request.put("number" , x.number())
                request.put("param" , x.param())
                request.put("levtype" , x.levtype())
                request.put("step" , x.step())
                request.put("grid" , x.grid())
                request.put("time" , x.time())
                request.put("date" , x.date())
                request.put("type" , x.xtype())
                request.put("class" , x.yclass())
                request.put("area" , x.area())
                request.put("format" , x.format())
                request.put("target" , x.target())
                
                if (x.levelist.supplied) request.put("levelist" , x.levelist())
                
                println(request)
                
                server.retrieve(request)     
                
            }else{
                println("Something wrong with arguments. Run with \"--help\" for more informations")
                x.printHelp
            }

        }catch {
          case e:Exception => { println("There was an error:\n")
                                e.printStackTrace(System.err)
                                x.printHelp
          }
        }

    }
    
}


//        val request = new JSONObject()
//
//        request.put("dataset" , "interim")
//        request.put("stream" , "oper")
//        request.put("param" , "165.128")    
//        request.put("levtype" , "sfc") 
//        request.put("step" , "0") 
//        request.put("grid" , "0.75/0.75") 
//        request.put("time" , "12") 
//        request.put("date" , "2013-01-01/to/2013-01-31")
//        request.put("type" , "an")
//        request.put("class" , "ei")
//
//
//         
////        request.put("dataset" , "tigge")
////        request.put("step"    , "24/to/120/by/24")
// //       request.put("number"  , "all")
//  //      request.put("levtype" , "sl")
//    //    request.put("date"    , "20121001/to/20121020")
//      //  request.put("time"    , "00")
////        request.put("origin"  , "all")
////        request.put("type"    , "pf")
//    //    request.put("param"   , "t")
//        request.put("area"    , "70/-130/30/-60")
////        request.put("grid"    , "2/2")
//        request.put("format"  , "netcdf")
//        request.put("target"  , "data3.nc")