//! Rust logging demo.

use log::{debug, error, info};

/// Logs a greeting.
fn main() {
    logger::init(
        logger::Config::default()
            .with_tag_on_device("rust")
            .with_min_level(log::Level::Trace),
    );
    debug!("Starting program.");
    info!("Things are going fine.");
    error!("Something went wrong!");
}

// use log::{debug, error, LevelFilter};
//
// fn main() {
//     let _init_success = logger::init(
//         logger::Config::default()
//             .with_tag_on_device("mytag")
//             .with_min_level(LevelFilter::Trace),
//     );
//     debug!("This is a debug message.");
//     error!("Something went wrong!");
// }
