package net.folab.metaj.jast;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.MethodVisitor;

import net.folab.metaj.bytecode.Java;
import net.folab.metaj.bytecode.JavaType;
import net.folab.metaj.bytecode.StatementContext;

public class AstWriter implements AstVisitor, Opcodes {

    private final Java java;

    private final ClassWriter cv;

    public AstWriter(Java java) {
        this.java = java;
        this.cv = new ClassWriter(false);
    }

    @Override
    public void visitClass(ClassDeclaration cd) {

        String[] interfaces = new String[cd.interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            interfaces[i] = cd.interfaces[i].getName();
        }

        cv.visit(java.version, // version
                cd.accessModifier.modifier, // access
                cd.name, // name
                null, // signature
                cd.superClass.getName(), // superName
                interfaces // interfaces
        );

        // TODO detect constructor of super class
        new MethodDeclaration(cd, "<init>") //
                .setParameterTypes() //
                .setReturnType(JavaType.VOID) //
                .addStatement( //
                        new ConstructorInvocation( //
                                "java/lang/Object", //
                                "()V", //
                                new LocalVariable("this") //
                        ) //
                ) //
                .addStatement(Return.VOID) //
                .accept(this);

        for (MemberDeclaration md : cd.mds) {
            if (md instanceof MethodDeclaration) {
                MethodDeclaration mm = (MethodDeclaration) md;
                mm.accept(this);
            }
        }

        cv.visitEnd();

    }

    @Override
    public void visitField(FieldDeclaration md) {
        String desc = md.type.getDescName();

        int modifier = md.access.modifier;
//        if (md.isStatic)
//            modifier += ACC_STATIC;
        cv.visitField(modifier, // access
                md.name, // name
                desc, // desc
                null, // signature
                null // value
        );

    }

    @Override
    public void visitMethod(MethodDeclaration md) {

        String desc = "(";
        for (JavaType pt : md.parameterTypes) {
            desc += pt.getDescName();
        }
        desc += ")";
        desc += md.returnType.getDescName();

        int modifier = md.access.modifier;
        if (md.block.isStatic)
            modifier += ACC_STATIC;
        MethodVisitor mv = cv.visitMethod(//
                modifier, // access
                md.name, // name
                desc, // desc
                null, // signature
                null // exceptions
                );

        mv.visitCode();

        StatementContext ctx = new StatementContext();

        if (!md.block.isStatic) {
            ctx.addLocal("this", new JavaType(md.cd.name));
        }

        for (Statement statement : md.block.statements) {
            statement.generate(mv, ctx);
        }

        mv.visitMaxs(ctx.maxStack(), ctx.maxLocals());
        mv.visitEnd();

    }

    @Override
    public byte[] toByteArray() {
        return cv.toByteArray();
    }

}
