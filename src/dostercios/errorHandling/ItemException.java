package dostercios.errorHandling;

public class ItemException extends Throwable {
	private int id;
	private String desc;

	public ItemException(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public void ItemNoEncontrado(){
		System.out.println("Item no encontrado");
	}

	@Override
	public String toString() {
		return "ItemException{" +
				"id=" + id +
				", desc='" + desc + '\'' +
				'}';
	}
}
