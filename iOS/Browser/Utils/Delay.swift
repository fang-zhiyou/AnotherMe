import Foundation

typealias Task = (Bool) -> ()

func delay(_ time: TimeInterval, task: @escaping () -> Void) -> Task? {
    func dispatchLater(block: @escaping () -> Void) {
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + time, execute: block)
    }
    
    var closure: Dispatch.os_block_t? = task
    var result: Task?
    
    let delayedClosure: Task = { cancel in
        if let internalClosure = closure {
            if cancel == false {
                DispatchQueue.main.async(execute: internalClosure)
            }
        }
        closure = nil
        result = nil
    }
    
    result = delayedClosure
    
    dispatchLater {
        if let delayedClosure = result {
            delayedClosure(false)
        }
    }
    
    return result
}

func cancel(_ task: Task?) {
    task?(true)
}
