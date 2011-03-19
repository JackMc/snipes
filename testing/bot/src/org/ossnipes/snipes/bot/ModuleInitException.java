package org.ossnipes.snipes.bot;

public class ModuleInitException extends ModuleException
{
	private static final long serialVersionUID = 5671671541122272824L;

	public ModuleInitException(Module erroredModule)
	{
		this.setErroredModule(erroredModule);
	}

	public ModuleInitException(String message, Module erroredModule)
	{
		super(message);
		this.setErroredModule(erroredModule);
	}

	public ModuleInitException(Throwable cause, Module erroredModule)
	{
		super(cause);
		this.setErroredModule(erroredModule);
	}

	public ModuleInitException(String message, Throwable cause,
			Module erroredModule)
	{
		super(message, cause);
		this.setErroredModule(erroredModule);
	}

	public Module getModule()
	{
		return this._erroredModule;
	}

	private void setErroredModule(Module erroredModule)
	{
		this._erroredModule = erroredModule;
	}

	private Module _erroredModule;

}
