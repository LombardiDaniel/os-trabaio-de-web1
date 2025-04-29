use std::net;
use std::sync::{mpsc, Arc, Mutex};
use std::thread;
use std::io::{Read, Write};
use hyper::Request;


static NUM_WORKER_THREADS: u32 = 12;

fn handle_connection(mut stream: net::TcpStream) {
    log::info!("Handling connection from: {:?}", stream.peer_addr().unwrap());
    
    let mut buf = [0; 2048];
    if let Some(err) = stream.read(&mut buf).err() {
        log::error!("Could not read stream from: {:?}: {:?}", stream.peer_addr(), err);
        return;
    }

    let req = Request::new(buf);
    println!("{:?}", req.headers());
}

fn main() {
    env_logger::Builder::new()
        .filter(None, log::LevelFilter::Info) // Set the default log level to `Info`
        .format(|buf, record| {
            writeln!(buf, "[{}] - {}", record.level(), record.args())
        })
        .init();
 
    let listener = net::TcpListener::bind("127.0.0.1:7878").unwrap();
    log::info!("Binding to http://127.0.0.1:7878/");

    let (tx, rx) = mpsc::channel();
    let rx = Arc::new(Mutex::new(rx));

    for _ in 0..NUM_WORKER_THREADS {
        let rx = Arc::clone(&rx);
        thread::spawn(move || {
            while let Ok(stream) = rx.lock().unwrap().recv() {
                handle_connection(stream);
            }
        });
    }

    for stream in listener.incoming() {
        match stream {
            Ok(stream) => {
                log::info!("Connection received from: {:?}", stream.peer_addr().unwrap());
                tx.send(stream).unwrap();
            }
            Err(e) => {
                log::error!("Failed to establish connection: {}", e);
                continue;
            }
        }
    }
}
