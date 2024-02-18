package coffee.mason.blacktar.html.impl;

import org.teavm.jso.dom.events.Event;

import coffee.mason.blacktar.component.Updatable;
import coffee.mason.blacktar.html.StyledButton;

public abstract class StyledButtonHeld extends StyledButton implements Updatable {

	public StyledButtonHeld(String renderString) {
		super(renderString);
		// TODO Auto-generated constructor stub
	}

	private boolean held;

	@Override
	public void onClick(Event e) {
		// ?
	}

	@Override
	public void update() {
		// On update, do on held
		if (held) {
			onHold();
		}
	}

	protected abstract void onHold();

	@Override
	public void handleTouchStart(Event e) {
		held = true;
	}

	@Override
	public void handleTouchMove(Event e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleTouchEnd(Event e) {
		held = false;
	}

	@Override
	public void handleTouchCancel(Event e) {
		held = false;
	}

	@Override
	public void handleTouchOut(Event e) {
		held = false;
	}

	
	// Styled button with implemented mouse events
	
}
